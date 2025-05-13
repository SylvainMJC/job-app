#!/bin/bash

# Configuration des variables
TEST_PLAN="/jmeter/test-plan/api-performance-test.jmx"
REPORT_DIR="/jmeter/reports"
RESULTS_FILE="/jmeter/results.jtl"

# Liste d'hôtes à essayer - nous allons tester chacun
HOSTS=("$1" "host.docker.internal" "172.17.0.1" "localhost")
DEFAULT_HOST=${1:-localhost}

PORT=${2:-8080}
PROTOCOL=${3:-http}
THREADS=${4:-3}
RAMPUP=${5:-1}
LOOPS=${6:-5}

echo "Running JMeter performance tests..."
echo "Configuration initiale: Host=$DEFAULT_HOST, Port=$PORT, Protocol=$PROTOCOL"
echo "Threads: $THREADS, Ramp-up: $RAMPUP, Loops: $LOOPS"

# Trouver un hôte accessible
WORKING_HOST=""
for HOST in "${HOSTS[@]}"; do
  echo "Testing connectivity to $HOST:$PORT..."
  if timeout 3 bash -c "echo > /dev/tcp/$HOST/$PORT" 2>/dev/null; then
    echo "Connection successful to $HOST:$PORT!"
    WORKING_HOST=$HOST
    break
  else
    echo "Connection failed to $HOST:$PORT"
  fi
done

if [ -z "$WORKING_HOST" ]; then
  echo "Could not connect to any host. Will try with the default host ($DEFAULT_HOST) anyway."
  WORKING_HOST=$DEFAULT_HOST
fi

echo "Using host: $WORKING_HOST for tests"

# Création du répertoire de rapport
mkdir -p $REPORT_DIR

# Exécution du test JMeter
echo "Starting JMeter test with reduced load (threads=$THREADS, loops=$LOOPS)..."
jmeter -n \
    -t $TEST_PLAN \
    -l $RESULTS_FILE \
    -e -o $REPORT_DIR \
    -Jhost=$WORKING_HOST \
    -Jport=$PORT \
    -Jprotocol=$PROTOCOL \
    -Jthreads=$THREADS \
    -Jrampup=$RAMPUP \
    -Jloops=$LOOPS

# Vérification du résultat
RESULT=$?
if [ $RESULT -eq 0 ]; then
    echo "Performance tests completed successfully"
    echo "Results available in $REPORT_DIR"
    
    # Extraction des métriques de performance clés
    echo "Performance Summary:"
    echo "---------------------------------------------------------"
    if [ -f "jmeter.log" ]; then
      grep "summary =" jmeter.log | tail -n 1 || echo "No summary found in log"
    else
      echo "No JMeter log file found"
    fi
    echo "---------------------------------------------------------"
    
    # Créer un résumé dans un fichier texte qui sera archivé
    echo "API Performance Test Summary" > $REPORT_DIR/summary.txt
    echo "Test Date: $(date)" >> $REPORT_DIR/summary.txt
    echo "Host: $WORKING_HOST" >> $REPORT_DIR/summary.txt
    echo "Threads: $THREADS" >> $REPORT_DIR/summary.txt
    echo "Loops: $LOOPS" >> $REPORT_DIR/summary.txt
    echo "---------------------------------------------------------" >> $REPORT_DIR/summary.txt
    if [ -f "jmeter.log" ]; then
      grep "summary =" jmeter.log | tail -n 1 >> $REPORT_DIR/summary.txt || echo "No summary data" >> $REPORT_DIR/summary.txt
    fi
    
    exit 0
else
    echo "Performance tests failed with code $RESULT"
    echo "Detailed error information:"
    
    # Créer un fichier d'erreur qui sera archivé
    echo "API Performance Test FAILED" > $REPORT_DIR/error.txt
    echo "Test Date: $(date)" >> $REPORT_DIR/error.txt
    echo "Host: $WORKING_HOST" >> $REPORT_DIR/error.txt
    echo "Exit Code: $RESULT" >> $REPORT_DIR/error.txt
    echo "---------------------------------------------------------" >> $REPORT_DIR/error.txt
    if [ -f "jmeter.log" ]; then
      tail -n 50 jmeter.log >> $REPORT_DIR/error.txt
    else
      echo "No JMeter log file found" >> $REPORT_DIR/error.txt
    fi
    
    exit 1
fi 