# --- Fase 1: Construcción (Build) ---
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# --- Fase 2: Preparación del Collector ---
FROM debian:bookworm-slim AS otel-fetcher
RUN apt-get update && apt-get install -y curl
RUN curl -L -o /otelcol https://github.com/open-telemetry/opentelemetry-collector-releases/releases/download/v0.90.0/otelcol-contrib_0.90.0_linux_amd64 && \
    chmod +x /otelcol

# --- Fase 3: Ejecución (Run) ---
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY --from=otel-fetcher /otelcol /app/otelcol

COPY otel-config-prod.yml /app/otel-config-prod.yml
COPY otel-config-dev.template.yml /app/otel-config-dev.template.yml

COPY entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
EXPOSE 8889

ENTRYPOINT ["./entrypoint.sh"]

ENTRYPOINT ["./entrypoint.sh"]