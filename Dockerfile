FROM gradle:jdk25-corretto AS build
WORKDIR /app

COPY gradlew gradle build.gradle settings.gradle gradle.properties /app/
COPY gradle /app/gradle
COPY build.gradle settings.gradle gradle.properties /app/
RUN chmod +x /app/gradlew

COPY src /app/src
RUN ./gradlew clean bootJar -x test

FROM eclipse-temurin:25-jre
WORKDIR /app

COPY --from=build /app/build/libs /app/libs
RUN rm -f /app/libs/*-plain.jar \
    && mv /app/libs/*.jar /app/app.jar \
    && rmdir /app/libs

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]