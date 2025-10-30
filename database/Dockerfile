# Use the official MySQL image from Docker Hub
FROM mysql:8.0

# Set environment variables for MySQL
ENV MYSQL_ROOT_PASSWORD=rootpassword
ENV MYSQL_DATABASE=complaint_db
ENV MYSQL_USER=complaint_user
ENV MYSQL_PASSWORD=complaint_pass

# Copy the init.sql file into the container at the location where MySQL looks for initialization scripts
COPY init.sql /docker-entrypoint-initdb.d/

# Expose MySQL port
EXPOSE 3306

# The MySQL image automatically runs the server, so no need to specify a command
