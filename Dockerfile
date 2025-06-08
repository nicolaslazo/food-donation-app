FROM maven:3.9.9-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY . .
# Create a fat JAR with all dependencies
RUN mvn clean compile assembly:single -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
# Copy the fat JAR with dependencies
COPY --from=build /app/target/*-jar-with-dependencies.jar app.jar
EXPOSE 8081
ENV DB_HOST=mysql
ENV DB_PORT=3306
ENV DB_USER=root
ENV DB_PASSWORD=mysql
# Run the application
CMD ["java", "-jar", "app.jar"]
