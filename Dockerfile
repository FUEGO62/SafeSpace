FROM maven:3-eclipse-temurin-21 AS build
COPY . .
RUN mvn clean package -DskipTests
FROM eclipse-temurin:21-alpine
COPY --from=build /target/hackerHouse-1.0-SNAPSHOT.jar hackerHouse.jar
ENTRYPOINT ["java","-Dspring.profiles.active=render","-jar","hackerHouse.jar"]
EXPOSE 9191
