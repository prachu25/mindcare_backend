# -------- Stage 1: Build --------
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# pom.xml copy
COPY pom.xml .

# dependencies cache
RUN mvn dependency:go-offline

# source code copy
COPY src ./src

# build jar
RUN mvn clean package -DskipTests


# -------- Stage 2: Run --------
FROM eclipse-temurin:17-jre

WORKDIR /app

# jar copy from build stage
COPY --from=build /app/target/*.jar app.jar

# port (Render internally map karega)
EXPOSE 8081

# run app
ENTRYPOINT ["java","-jar","app.jar"]
