FROM amazoncorretto:11-alpine-jdk
COPY target/conference-demo-0.0.1-SNAPSHOT.jar conference-demo-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/conference-demo-0.0.1-SNAPSHOT.jar"]