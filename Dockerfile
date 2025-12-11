# --- STAGE 1: Build the Application ---
# Using Maven with Java 25 support
FROM maven:3.9-eclipse-temurin-25-alpine AS build
WORKDIR /app

# 1. Copy pom.xml first to cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 2. Copy source code and build
COPY src ./src
# Spring Boot 4 build command
RUN mvn clean package -DskipTests

# --- STAGE 2: Run the Application ---
# Using the lightweight Java 25 JRE (Alpine)
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# Copy the built JAR from Stage 1
COPY --from=build /app/target/*.jar app.jar

# Create non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Expose the standard port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]