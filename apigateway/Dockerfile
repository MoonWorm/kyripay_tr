FROM openjdk:8u212-jre
ARG JAR_FILE
COPY ${JAR_FILE} /usr/local/lib/apigateway.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/apigateway.jar"]
EXPOSE 9090