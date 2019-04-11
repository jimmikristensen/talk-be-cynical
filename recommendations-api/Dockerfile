FROM openjdk:11-jdk-slim
RUN apt-get update && apt-get install -y curl
COPY build/libs/*-all.jar recommendations-api.jar
CMD java ${JAVA_OPTS} -jar recommendations-api.jar