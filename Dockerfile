FROM maven:3.8.3-openjdk-17 AS builder

COPY . /app

WORKDIR /app

RUN ["mvn", "clean", "install"]

FROM openjdk:17

COPY --from=builder /app/target/fileserver-1.0.3.jar /application/fileserver-1.0.3.jar

WORKDIR /application

CMD ["java", "-jar", "-Dspring.profiles.active=production", "fileserver-1.0.3.jar"]
