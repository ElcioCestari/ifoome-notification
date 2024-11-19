# Use a lightweight base image with OpenJDK and Maven
FROM ubuntu:latest
LABEL authors="elcio"

WORKDIR /app

# Update package list and install Maven and OpenJDK 17
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk maven && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Copy the source code into the container
COPY . .

# Build the application with Maven
RUN mvn clean install

# Define environment variables
ENV PORT=8082
ENV URL_DISCOVERY=discovery

# Expose the application port
EXPOSE 8082

# Use the generated JAR from the `target` directory (update the file name if necessary)
ENTRYPOINT ["java", "-jar", "target/ifoome-notification-0.0.1-SNAPSHOT.jar"]
