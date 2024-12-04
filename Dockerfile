FROM openjdk:21-jdk-oracle
LABEL authors="Anouar_bel"

WORKDIR /app
VOLUME /tmp
COPY target/*.jar /app/spring-security.jar


ENTRYPOINT ["java", "-jar", "/app/spring-security.jar"]