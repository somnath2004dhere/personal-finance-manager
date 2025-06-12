# Step 1: Use Maven to build the project
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Step 2: Use a smaller image to run the jar
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/personal-finance-manager-0.0.1-SNAPSHOT.jar app.jar

# Command to run the app
ENTRYPOINT ["java", "-jar", "app.jar"]

