FROM amazoncorretto:17-jdk-alpine
VOLUME /tmp
RUN ./gradlew bootJar
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080