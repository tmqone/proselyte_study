FROM gradle:jdk25-corretto AS build
WORKDIR /app

COPY gradlew /app/gradlew
COPY gradle /app/gradle
COPY build.gradle settings.gradle gradle.properties /app/
RUN chmod +x /app/gradlew

COPY src /app/src
RUN ./gradlew clean bootJar -x test

FROM eclipse-temurin:25-jre
WORKDIR /app

ENV SPRING_PROFILES_ACTIVE=docker \
    POSTGRES_PORT=5432 \
    POSTGRES_DB=changeme \
    POSTGRES_USER=changeme \
    POSTGRES_PASSWORD=changeme \
    JWT_SECRET=changeme \
    JWT_EXPIRATION=3600 \
    JWT_ISSUER=app \
    MINIO_PORT=9000 \
    MINIO_ROOT_USER=changeme \
    MINIO_ROOT_PASSWORD=changeme

COPY --from=build /app/build/libs /app/libs
RUN rm -f /app/libs/*-plain.jar \
    && mv /app/libs/*.jar /app/app.jar \
    && rmdir /app/libs

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]