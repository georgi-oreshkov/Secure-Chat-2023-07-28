FROM bellsoft/liberica-openjdk-alpine:latest
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]