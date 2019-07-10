FROM openjdk:8-jdk-alpine
COPY build/libs/users*.jar /usr/src/app.jar
CMD java -jar /usr/src/app.jar

# docker build . -t users-service
# docker run --name users-service -p 8085:8080 -d users-service
