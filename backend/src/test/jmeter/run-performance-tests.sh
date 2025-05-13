#!/bin/bash

# Configuration des variables
TEST_PLAN="api-performance-test.jmx"
REPORT_DIR="/jmeter/reports"
RESULTS_FILE="/jmeter/results.jtl"
HOST=${1:-localhost}
PORT=${2:-8080}
PROTOCOL=${3:-http}
THREADS=${4:-20}
RAMPUP=${5:-10}
DURATION=${6:-60}

echo "Running JMeter performance tests..."
echo "Host: $HOST, Port: $PORT, Protocol: $PROTOCOL"
echo "Threads: $THREADS, Ramp-up: $RAMPUP, Duration: $DURATION seconds"

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