FROM openjdk:8-jdk-alpine
COPY ./build/libs/traces.jar traces.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=docker","/traces.jar"]
EXPOSE 8090