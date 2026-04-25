# --- Etapa 1: Build (Compilación) ---
# Usamos Maven con Eclipse Temurin 17
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Seteamos el encoding a nivel de sistema operativo del contenedor
ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests -Dproject.build.sourceEncoding=UTF-8

# --- Etapa 2: Run (Ejecución) ---
# Usamos JRE de Temurin
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8081

ENV SPRING_DATA_MONGODB_URI=""

ENTRYPOINT ["java", "-jar", "app.jar"]