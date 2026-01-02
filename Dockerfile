FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY ./target/al-baraka-0.0.1-SNAPSHOT.war app.war

EXPOSE 8081

ENTRYPOINT [ "java" , "-jar" , "app.war"]