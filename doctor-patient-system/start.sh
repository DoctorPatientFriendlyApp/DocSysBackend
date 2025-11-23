#!/bin/bash
# Build the project
./mvnw clean package -DskipTests

# Run the Spring Boot jar
java -jar target/doctor-patient-system-0.0.1-SNAPSHOT.jar

