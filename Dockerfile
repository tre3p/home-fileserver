FROM maven:3.8.3-openjdk-17 AS builder

COPY . /app

WORKDIR /app

RUN ["mvn", "clean", "package", "-P", "production"]

FROM openjdk:17

COPY --from=builder /app/target/filserver-1.0.3.jar /application

CMD ["java", "-jar", "-Xmx8g", "-Dspring.profiles.active=production", "fileserver-1.0.3.jar"]
