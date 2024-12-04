# Use a lightweight runtime image
FROM amazoncorretto:17-alpine3.18

# Expose the application port
EXPOSE 8080

# Volume for temporary files
VOLUME /tmp

# Copy the built JAR from the builder stage
COPY build/libs/*.jar app.jar

# Set the entry point for the application
ENTRYPOINT ["java", "-jar", "/app.jar"]
