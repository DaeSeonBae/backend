FROM openjdk:17-jdk-alpine
VOLUME /tmp
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java","-Dspring.profiles.active=aws","-jar","/app.jar"]
