# Use lightweight Java 21 JDK image
FROM eclipse-temurin:21-jdk-alpine

# Create app directory
WORKDIR /app

# Copy the JAR file
COPY target/Booking-0.0.1-SNAPSHOT.jar app.jar

# Expose the Spring Boot app port
EXPOSE 8076

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
