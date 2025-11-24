# Use a Java 21 base image
FROM eclipse-temurin:21-jdk-jammy

# Set working directory inside container
WORKDIR /app

# Copy all project files into container
COPY . .

# Build the Spring Boot jar
RUN ./mvnw clean package -DskipTests

# Expose port 8080
EXPOSE 8080

# Command to run the jar
CMD ["java", "-jar", "target/doctor-patient-system-0.0.1-SNAPSHOT.jar"]
