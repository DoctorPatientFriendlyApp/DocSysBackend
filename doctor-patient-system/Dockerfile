# Use Java 21 JDK
FROM eclipse-temurin:21-jdk-jammy

# Set working directory
WORKDIR /app

# Copy all project files
COPY . .

# Make mvnw executable
RUN chmod +x mvnw

# Build the Spring Boot jar
RUN ./mvnw clean package -DskipTests

# Expose port
EXPOSE 8080

# Run the jar
CMD ["java", "-jar", "target/doctor-patient-system-0.0.1-SNAPSHOT.jar"]
