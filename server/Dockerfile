FROM amazoncorretto:11

COPY target/*.jar /server.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/server.jar"]