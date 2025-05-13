#!/bin/bash

# Configuration des variables
TEST_PLAN="/jmeter/test-plan/api-performance-test.jmx"
REPORT_DIR="/jmeter/reports"
RESULTS_FILE="/jmeter/results.jtl"

# Configuration simplifiée
HOST=${1:-localhost}
PORT=${2:-8080}
PROTOCOL=${3:-http}
THREADS=${4:-2}
RAMPUP=${5:-1}
LOOPS=${6:-3}

echo "Running JMeter performance tests..."
echo "Host: $HOST, Port: $PORT, Protocol: $PROTOCOL"
echo "Threads: $THREADS, Ramp-up: $RAMPUP, Loops: $LOOPS"

# Vérifier la connectivité avant de lancer JMeter
echo "=== CONNECTIVITY TEST ==="
echo "Testing connection to $HOST:$PORT..."

# Utiliser netcat si disponible
if command -v nc >/dev/null 2>&1; then
  if nc -z -v $HOST $PORT 2>/dev/null; then
    echo "Connection successful via netcat!"
  else
    echo "Connection failed via netcat"
  fi
fi

# Test de connectivité basique
if curl -s $PROTOCOL://$HOST:$PORT/api/offres -o /dev/null; then
  echo "Curl test successful to $PROTOCOL://$HOST:$PORT/api/offres"
else
  echo "Curl test failed to $PROTOCOL://$HOST:$PORT/api/offres (error code: $?)"
  curl -v $PROTOCOL://$HOST:$PORT/api/offres || echo "Connection test failed"
fi

# Afficher les informations réseau
echo "=== NETWORK INFORMATION ==="
echo "Container hostname: $(hostname)"
echo "IP addresses:"
ip addr show || hostname -I || echo "No IP tool available"
echo "Route table:"
netstat -rn || route || ip route || echo "No routing tool available"
echo "DNS resolution:"
cat /etc/resolv.conf || echo "No resolv.conf file"
echo "Host resolution:"
getent hosts $HOST || echo "Host $HOST not in hosts database"
echo "=== END NETWORK INFORMATION ==="

# Création du répertoire de rapport
mkdir -p $REPORT_DIR

# Exécution du test JMeter
echo "Starting JMeter test (threads=$THREADS, loops=$LOOPS)..."
jmeter -n \
    -t $TEST_PLAN \
    -l $RESULTS_FILE \
    -e -o $REPORT_DIR \
    -Jhost=$HOST \
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
    exit 0
else
    echo "Performance tests failed with code $RESULT"
    echo "Check the error logs for more details"
    
    # Sauvegarder une copie du fichier journal dans le répertoire des rapports
    echo "Test failure at $(date)" > $REPORT_DIR/error.txt
    if [ -f "jmeter.log" ]; then
      echo "=== JMETER LOG EXTRACT ===" >> $REPORT_DIR/error.txt
      tail -n 50 jmeter.log >> $REPORT_DIR/error.txt
    fi
    
    exit 1
fi 