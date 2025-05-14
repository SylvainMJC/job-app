FROM gradle:7.6.1-jdk17 AS build

WORKDIR /app

COPY . .

RUN java -version && gradle --version

ENV JAVA_HOME=/opt/java/openjdk

RUN gradle build -x test --info

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"] 