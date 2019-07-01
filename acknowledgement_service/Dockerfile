FROM openjdk:11-jre-slim

ARG VERSION

COPY acknowledgement-${VERSION}.jar /usr/src/acknowledgement.jar

CMD java -jar /usr/src/acknowledgement.jar

EXPOSE 8080