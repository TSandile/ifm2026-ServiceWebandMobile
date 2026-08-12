# =================== Build Image ===================
FROM maven:3.9-eclipse-temurin-25 AS build

WORKDIR /app

COPY pom.xml
RUN mvn dependancy:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

# =================== Run Container =================
FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=build /app/target/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]