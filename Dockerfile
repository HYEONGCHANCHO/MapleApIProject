#FROM bellsoft/liberica-openjdk-alpine:11
## or
## FROM openjdk:8-jdk-alpine
##FROM openjdk:11-jdk-alpine
#
#CMD ["./gradlew", "clean", "build"]
## or Maven
## CMD ["./mvnw", "clean", "package"]
#
#VOLUME /tmp
#
#ARG JAR_FILE=build/libs/*.jar
## or Maven
## ARG JAR_FILE_PATH=target/*.jar
#
#COPY ${JAR_FILE} app.jar
#
#COPY src/main/resources/application.yml /config/application.yml
#
#
#EXPOSE 8080
#
#ENTRYPOINT ["java","-jar","/app.jar"]
#FROM bellsoft/liberica-openjdk-alpine:11
#FROM openjdk:11-alpine
#FROM openjdk:11-jdk-alpine
FROM openjdk:11-jdk-slim

WORKDIR /app

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar

#COPY src/main/resources/application.yml /config/application.yml

#EXPOSE 8080

#ENTRYPOINT ["java", "-jar", "/build/libs/*.jar", "--spring.config.location=file:/config/application.yml"]
ENTRYPOINT ["java", "-jar", "app.jar"]
