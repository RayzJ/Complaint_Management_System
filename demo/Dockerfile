FROM openjdk:17-jdk-slim

WORKDIR /app

# Install wait-for-it script for database dependency
RUN apt-get update && apt-get install -y wget && \
    wget https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh && \
    chmod +x wait-for-it.sh

COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["./wait-for-it.sh", "mysql:3306", "--timeout=60", "--", "java", "-jar", "app.jar"]