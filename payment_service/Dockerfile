FROM openjdk:8u212-jre
COPY target/payment-0.0.1-SNAPSHOT.jar /usr/local/lib/payment.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/payment.jar"]
EXPOSE 8080