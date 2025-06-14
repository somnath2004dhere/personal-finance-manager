# ------------ Build stage ------------
FROM maven:3.9.4-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml and download dependencies first (layer caching)
COPY pom.xml .
COPY src ./src

# Build the project
RUN mvn clean package -DskipTests

# ------------ Run stage ------------
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy built jar from build stage
COPY --from=build /app/target/personal-finance-manager-0.0.1-SNAPSHOT.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]




