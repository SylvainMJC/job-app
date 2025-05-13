FROM alpine:3.18

# Variables pour les versions
ENV JMETER_VERSION="5.5"
ENV JMETER_HOME="/opt/apache-jmeter-${JMETER_VERSION}"
ENV JMETER_BIN="${JMETER_HOME}/bin"
ENV PATH="$JMETER_BIN:$PATH"

# Installation des dépendances et outils de diagnostic réseau
RUN apk update && \
    apk add --no-cache ca-certificates openjdk11-jre wget bash grep \
    curl iputils bind-tools netcat-openbsd net-tools iproute2

# Téléchargement et installation de JMeter
RUN mkdir -p /opt && \
    wget -q https://archive.apache.org/dist/jmeter/binaries/apache-jmeter-${JMETER_VERSION}.tgz -O /tmp/apache-jmeter-${JMETER_VERSION}.tgz && \
    tar -xzf /tmp/apache-jmeter-${JMETER_VERSION}.tgz -C /opt && \
    rm /tmp/apache-jmeter-${JMETER_VERSION}.tgz

# Création des répertoires de travail
RUN mkdir -p /jmeter/test-plan /jmeter/reports

# Copie des fichiers de test
COPY api-performance-test.jmx /jmeter/test-plan/
COPY run-performance-tests.sh /jmeter/

# Vérification de la présence des fichiers et permissions
RUN chmod +x /jmeter/run-performance-tests.sh && \
    ls -la /jmeter/test-plan/ && \
    ls -la /jmeter/

# Définir le répertoire de travail
WORKDIR /jmeter

# Commande par défaut
ENTRYPOINT ["/jmeter/run-performance-tests.sh"]

# Paramètres par défaut
CMD ["localhost", "8080", "http", "2", "1", "3"] 