# Use the official OpenJDK image as a parent image
FROM openjdk:17-jdk-slim
# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container at /app
COPY first-app*.jar app.jar

COPY lib /app/lib

# Copy application properties from the config directory into the container
COPY application.properties /app/application.properties

# Expose port 8080 for the Spring Boot application
EXPOSE 8080


# Specify the command to run the Spring Boot application
CMD ["java", "-jar", "app.jar"]
