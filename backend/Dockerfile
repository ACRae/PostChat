FROM ubuntu:latest

USER root

ENV DEBIAN_FRONTEND=noninteractive

# Update package lists and install OpenJDK 17
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk python3.10 python3-pip

# Set Java environment variables
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk
ENV PATH=$PATH:$JAVA_HOME/bin

# Set the working directory
WORKDIR /app

# Copy the JAR file
COPY ./compiled/PostChatBackend-*.jar /app/PostChatBackend.jar
COPY ./templates /app/templates
COPY ./ai/htr /app/htr

RUN pip install -r /app/htr/requirements.txt

ENV POSTCHAT_TEMPLATES=/app/templates
ENV POSTCHAT_HTR=/app/htr/src

EXPOSE 9000

# Specify the command to run your application
CMD ["java", "-jar", "PostChatBackend.jar"]