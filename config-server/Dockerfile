FROM openjdk:17-jdk-slim-buster
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} server.jar
EXPOSE 8070
ENTRYPOINT ["java","-jar","/server.jar"]