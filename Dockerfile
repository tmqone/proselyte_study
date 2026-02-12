FROM gradle:jdk25-corretto AS build
WORKDIR /app

COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle settings.gradle gradle.properties ./

RUN sed -i 's/\r$//' gradlew && chmod +x gradlew

COPY src src
RUN ./gradlew clean bootJar -x test

FROM eclipse-temurin:25-jre
WORKDIR /app

COPY --from=build /app/build/libs /app/libs
RUN rm -f /app/libs/*-plain.jar \
    && mv /app/libs/*.jar /app/app.jar \
    && rmdir /app/libs

EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=docker" ,"-jar", "/app/app.jar"]
