FROM openjdk:17

WORKDIR /app

ARG JAR_FILE

COPY ${JAR_FILE} /app/app.jar

CMD ["java", "-jar", "app.jar"]
