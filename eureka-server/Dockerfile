FROM openjdk:11-jre-slim

ARG VERSION

COPY target/eureka-server-${VERSION}.jar /usr/src/eureka-server.jar

CMD java -jar /usr/src/eureka-server.jar

EXPOSE 9090