FROM openjdk:8u212-jre
ARG JAR_FILE
COPY ${JAR_FILE} /usr/local/lib/payment.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/payment.jar"]
EXPOSE 8080