# ----------- Stage 1: Build the application -----------
FROM maven:3.9.9-amazoncorretto-21 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy pom.xml and download dependencies first (for better caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build the app
COPY src ./src
RUN mvn clean package -DskipTests

# ----------- Stage 2: Run the application -----------
FROM amazoncorretto:21-alpine

# Set working directory
WORKDIR /app

# Copy the built jar file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose application port (change if needed)
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]