FROM adoptopenjdk/openjdk11:alpine
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
COPY config/application.yml config/application.yml
RUN mkdir /storage
ENTRYPOINT ["java", "-jar", "/app.jar"]