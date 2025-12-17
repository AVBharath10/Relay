FROM eclipse-temurin:21-jre


WORKDIR /app

#copy jar
COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]