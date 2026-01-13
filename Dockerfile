# Use a multi-stage build to optimize the image size

# Stage 1: Build the application
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:21-jre
WORKDIR /app
# Der Name muss exakt zur artifactId und Version in der pom.xml passen 
COPY --from=build /app/target/projectify-pro-backend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]