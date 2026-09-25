# Stage 1 : build
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copier uniquement le pom.xml d'abord pour profiter du cache Docker
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copier le code source et builder
COPY src ./src
RUN mvn clean package -DskipTests -B

# Stage 2 : runtime
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8282
ENTRYPOINT ["java", "-jar", "app.jar"]