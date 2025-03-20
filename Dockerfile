# ===== STAGE 1: Build the application =====
FROM maven:3.9.5-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy the pom.xml and download dependencies (this is cached)
COPY pom.xml ./
RUN mvn dependency:go-offline -B

# Copy the source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# ===== STAGE 2: Create the final image =====
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy only the built JAR file from the builder stage
COPY --from=builder /app/target/environmental-data-service-0.0.1-SNAPSHOT.jar /app/environmental-data-service-0.0.1-SNAPSHOT.jar

# Verify the file is copied correctly
RUN ls -l /app

# Set the entrypoint
ENTRYPOINT ["java", "-jar", "/app/environmental-data-service-0.0.1-SNAPSHOT.jar"]
