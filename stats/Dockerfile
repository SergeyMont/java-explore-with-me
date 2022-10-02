FROM amazoncorretto:11

COPY target/*.jar /stats.jar

EXPOSE 9090

ENTRYPOINT ["java", "-jar", "/stats.jar"]