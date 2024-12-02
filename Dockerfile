FROM openjdk:17-jdk-slim

WORKDIR /app
COPY target/environmental-data-service-0.0.1-SNAPSHOT.jar /app/environmental-data-service-0.0.1-SNAPSHOT.jar

RUN ls -l /app

ENTRYPOINT ["java", "-jar", "/app/environmental-data-service-0.0.1-SNAPSHOT.jar"]
