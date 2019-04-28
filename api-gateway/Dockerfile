FROM openjdk:11-jdk-slim
RUN apt-get update && apt-get install -y curl && apt-get install -y inetutils-ping
COPY build/libs/*-all.jar api-gateway.jar
CMD java ${JAVA_OPTS} -jar api-gateway.jar