#!/bin/bash

# Configuration des variables
TEST_PLAN="/jmeter/test-plan/api-performance-test.jmx"
REPORT_DIR="/jmeter/reports"
RESULTS_FILE="/jmeter/results.jtl"

# Si l'hôte est "localhost", essayer d'utiliser host.docker.internal
HOST=${1:-localhost}
if [ "$HOST" = "localhost" ]; then
  echo "Trying to use host.docker.internal instead of localhost"
  ping -c 1 host.docker.internal > /dev/null 2>&1
  if [ $? -eq 0 ]; then
    HOST="host.docker.internal"
    echo "Using host.docker.internal to connect to the host machine"
  else
    echo "host.docker.internal not resolvable, falling back to original host"
  fi
fi

PORT=${2:-8080}
PROTOCOL=${3:-http}
THREADS=${4:-20}
RAMPUP=${5:-10}
DURATION=${6:-60}

echo "Running JMeter performance tests..."
echo "Host: $HOST, Port: $PORT, Protocol: $PROTOCOL"
echo "Threads: $THREADS, Ramp-up: $RAMPUP, Duration: $DURATION seconds"

# Vérifier la connectivité avant d'exécuter le test
echo "Testing connectivity to $HOST:$PORT..."
timeout 5 bash -c "cat < /dev/null > /dev/tcp/$HOST/$PORT"
if [ $? -eq 0 ]; then
  echo "Connection successful!"
else
  echo "Connection failed! Attempting to continue anyway..."
fi

# Création du répertoire de rapport
mkdir -p $REPORT_DIR

# Exécution du test JMeter
jmeter -n \
    -t $TEST_PLAN \
    -l $RESULTS_FILE \
    -e -o $REPORT_DIR \
    -Jhost=$HOST \
    -Jport=$PORT \
    -Jprotocol=$PROTOCOL \
    -Jthreads=$THREADS \
    -Jrampup=$RAMPUP \
    -Jduration=$DURATION

# Vérification du résultat
if [ $? -eq 0 ]; then
    echo "Performance tests completed successfully"
    echo "Results available in $REPORT_DIR"
    
    # Extraction des métriques de performance clés
    echo "Performance Summary:"
    echo "---------------------------------------------------------"
    grep "summary =" jmeter.log | tail -n 1
    echo "---------------------------------------------------------"
    
    exit 0
else
    echo "Performance tests failed"
    exit 1
fi 