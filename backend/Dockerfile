FROM gradle:7.6.1-jdk17 AS build

WORKDIR /app

COPY . .

# Print environment information for debugging
RUN java -version && gradle --version

# Set JAVA_HOME environment variable to ensure Gradle uses Java 17
ENV JAVA_HOME=/opt/java/openjdk

# Build the application with debug output
RUN gradle build -x test --info

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"] 