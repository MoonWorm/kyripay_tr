FROM openjdk:11-jre-slim

ARG VERSION

COPY target/apigateway-${VERSION}.jar /usr/src/apigateway.jar

CMD java -jar /usr/src/apigateway.jar

EXPOSE 9090