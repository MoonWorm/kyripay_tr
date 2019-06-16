FROM openjdk:11-jre-slim

ARG VERSION

COPY converter-${VERSION}.jar /usr/src/converter-${VERSION}.jar

CMD java -jar /usr/src/converter-${VERSION}.jar

EXPOSE 8080