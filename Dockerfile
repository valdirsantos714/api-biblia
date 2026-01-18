FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app

COPY pom.xml .
COPY src ./src
COPY biblia.sql .

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar
COPY --from=builder /app/biblia.sql biblia.sql

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
