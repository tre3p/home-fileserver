FROM openjdk:17

COPY target/fileserver-1.0.3.jar .

CMD ["java", "-jar", "-Xmx8g", "-Dspring.profiles.active=production", "fileserver-1.0.3.jar"]
