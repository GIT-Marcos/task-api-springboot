#!/bin/sh
if [ "$APP_PROFILE" = "prod" ]; then
    CONFIG_FILE="/app/otel-config-prod.yml"
else
    CONFIG_FILE="/app/otel-config-dev.yml"
fi

echo "Iniciando OTEL Collector con: $CONFIG_FILE"
/app/otelcol --config=$CONFIG_FILE &

echo "Iniciando Aplicación Java..."
java -jar app.jar