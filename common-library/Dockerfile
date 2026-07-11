FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/*.jar common.jar

EXPOSE 8089

ENTRYPOINT ["java", "-jar", "common.jar"]