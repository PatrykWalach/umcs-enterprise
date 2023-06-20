FROM gradle:latest as BUILD
WORKDIR /usr/app/
COPY . .
RUN gradle bootJar

FROM amazoncorretto:17-alpine-jdk
ENV JAR_NAME=app.jar
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY --from=BUILD $APP_HOME .
EXPOSE 8080
ENTRYPOINT exec java -jar $APP_HOME/build/libs/enterprise-0.0.1-SNAPSHOT.jar