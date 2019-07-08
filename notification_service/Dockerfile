FROM openjdk:8u212-jre
ARG JAR_FILE
COPY ${JAR_FILE} /usr/local/lib/notification.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/notification.jar"]
EXPOSE 8080