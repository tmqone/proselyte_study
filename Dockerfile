FROM maven AS build
WORKDIR /app
COPY . /app
RUN mvn clean package -Pdocker -DskipTests

FROM tomcat:jre25-temurin
WORKDIR /usr/local/tomcat
COPY --from=build /app/target/proselyte_study-1.0-SNAPSHOT.war ./webapps/ROOT.war
EXPOSE 8080
ENTRYPOINT ["./bin/catalina.sh"]
CMD ["run"]
