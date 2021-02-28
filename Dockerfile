FROM adoptopenjdk/openjdk11:alpine
WORKDIR /usr/app
COPY . .
RUN ./gradlew release
ARG JAR_FILE=build/libs/ByteKeeperService-0.0.1-SNAPSHOT.jar
RUN mv ${JAR_FILE} app.jar
RUN mkdir storage
ENTRYPOINT ["java", "-jar", "./app.jar"]