FROM gradle:9.3.1-jdk25 AS build
WORKDIR /home/gradle/project

ARG NEXUS_URL=http://host.docker.internal:9081
ENV NEXUS_URL=${NEXUS_URL}

COPY ./person-service ./person-service/

WORKDIR /home/gradle/project/person-service
RUN gradle --no-daemon clean bootJar

FROM eclipse-temurin:25.0.2_10-jre

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY --from=build /home/gradle/project/person-service/build/libs/*.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]