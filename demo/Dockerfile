FROM openjdk:17-jdk-slim

WORKDIR /app

# Install Maven
RUN apt-get update && apt-get install -y maven wget && rm -rf /var/lib/apt/lists/*

# Copy source and build
COPY . .
RUN mvn clean package -DskipTests && mv target/*.jar app.jar

# Install wait-for-it
RUN wget -O wait-for-it.sh https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh && chmod +x wait-for-it.sh

EXPOSE 8080

CMD ["./wait-for-it.sh", "mysql:3306", "--timeout=60", "--", "java", "-jar", "app.jar"]