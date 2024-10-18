FROM eclipse-temurin:21

# Set the working directory in the container
WORKDIR /todo-api

# Copy the Spring Boot JAR file into the container
COPY target/*.jar /todo-api/todo-api.jar

# Expose the port that your Spring Boot application is running on
EXPOSE 8080

# Set environment variables (optional but recommended)
ENV JAVA_OPTS=""

# Command to run the Spring Boot application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /todo-api/todo-api.jar"]