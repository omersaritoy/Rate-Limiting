FROM maven:3.9.9-eclipse-temurin-25 as build

WORKDIR /app

copy pom.xml .

RUN mvn dependency:go-offline -B

Copy src ./src


from eclipse-temurin:25-jre-alpine

WORKDIR /app

copy --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]

