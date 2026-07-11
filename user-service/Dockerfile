FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/*.jar user.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "user.jar"]