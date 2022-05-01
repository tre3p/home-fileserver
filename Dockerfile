FROM openjdk:17

COPY . /usr/src/app

WORKDIR /usr/src/app

RUN ./mvnw clean package -DskipTests

COPY target/fileserver-1.0.1.jar /usr/src/app

CMD ["java", "-jar", "fileserver-1.0.1.jar"]
