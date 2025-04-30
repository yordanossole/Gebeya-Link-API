FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml mvnw ./
COPY .mvn .mvn/
COPY src src/
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/dreamShops-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your app listens on
EXPOSE 8080

# Launch the application
ENTRYPOINT ["java", "-jar", "app.jar"]