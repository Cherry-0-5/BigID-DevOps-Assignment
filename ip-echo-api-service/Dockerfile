# Runner Stage Module
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Create a non-root user-group
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copy the pre-built JAR from the Jenkins workspace.
COPY target/*.jar app.jar

# Adjust permissions for the JAR file
RUN chown appuser:appgroup app.jar

# Switch to non-root user
USER appuser

# Exposing backend api server port
EXPOSE 8088

# Starting Java backend application
ENTRYPOINT ["java", "-jar", "app.jar"]
