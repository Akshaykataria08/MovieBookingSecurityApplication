FROM openjdk:14-alpine
ADD /target/SpringMVCWebApp-0.0.1-SNAPSHOT.jar SpringMVCWebApp-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "SpringMVCWebApp-0.0.1-SNAPSHOT.jar"]