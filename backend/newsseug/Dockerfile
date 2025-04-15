FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/*.jar app.jar

CMD ["java", "-Dspring.profiles.active=dev", "-jar", "app.jar"]