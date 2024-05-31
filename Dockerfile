FROM openjdk:17-jdk-slim
ADD /build/libs/*.jar app.jar
ENTRYPOINT ["java","-Djava.securityegd=file:/dev/./urandom","-jar","/app.jar"]