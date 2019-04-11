FROM openjdk:11-jdk-slim
RUN apt-get update && apt-get install -y curl
COPY build/libs/*-all.jar content-api.jar
CMD java ${JAVA_OPTS} -jar content-api.jar