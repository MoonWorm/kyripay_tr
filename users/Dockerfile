FROM openjdk:8-jdk-alpine
COPY build/libs/users*.jar /usr/src/app.jar
CMD java -jar /usr/src/app.jar

# docker run --name users -p 8082:8080 -d users-service
