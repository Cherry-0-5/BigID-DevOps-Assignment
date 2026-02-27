# Build Stage Module
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source files and build final JAR package
COPY src ./src
RUN mvn clean package -DskipTests

# Runner Stage Module
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Create a non-root user-group for security and copy built package using new user permission
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
COPY --chown=appuser:appgroup --from=build /app/target/*.jar app.jar

# Switch to non-root user
USER appuser

# Exposing backend api server port
EXPOSE 8088

# Starting Java backend application
ENTRYPOINT ["java", "-jar", "app.jar"]
