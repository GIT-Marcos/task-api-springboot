# --- Fase 1: Construcción (Build) ---
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# --- Fase 2: Obtener el Collector desde la IMAGEN OFICIAL ---
FROM otel/opentelemetry-collector-contrib:0.90.0 AS otel-bin

# --- Fase 3: Ejecución (Run) ---
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY --from=otel-bin /otelcol /app/otelcol

COPY otel-config-prod.yml /app/otel-config-prod.yml
COPY otel-config-dev.template.yml /app/otel-config-dev.template.yml

COPY entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["./entrypoint.sh"]