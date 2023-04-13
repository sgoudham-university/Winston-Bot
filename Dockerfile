# Build
FROM maven:3.8.6-amazoncorretto-17 AS build

ADD ./pom.xml pom.xml
ADD ./src src/

RUN mvn clean package

# Runtime
FROM amazoncorretto:17-alpine-jdk

COPY --from=build target/winston.jar winston.jar

EXPOSE 8080

CMD ["java", "-jar", "winston.jar"]