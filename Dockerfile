# --- Fase 1: Construcción (Build) ---
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# --- Fase 2: Preparación del Collector ---
FROM alpine:latest AS otel-fetcher
RUN apk add --no-cache curl
# Descargamos el binario (contrib-linux-amd64)
RUN curl -L -o /otelcol https://github.com/open-telemetry/opentelemetry-collector-releases/releases/download/v0.90.0/otelcol-contrib_0.90.0_linux_amd64 && \
    chmod +x /otelcol

# --- Fase 3: Ejecución (Run) ---
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copiamos el binario del Collector
COPY --from=otel-fetcher /otelcol /app/otelcol
# Copiamos la configuración y el script (que debes crear en tu repo)
COPY otel-config.yml /app/otel-config.yml
COPY entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh

# Copiamos el JAR desde la fase de build
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
# El puerto 8889 de Prometheus solo se expone en DEV
EXPOSE 8889

ENTRYPOINT ["./entrypoint.sh"]