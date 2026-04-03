#!/bin/sh
if [ "$APP_PROFILE" = "prod" ]; then
    CONFIG_FILE="/app/otel-config-prod.yml"
else
    if [ -f "/app/otel-config.yml" ]; then
        CONFIG_FILE="/app/otel-config.yml"
    else
        CONFIG_FILE="/app/otel-config-dev.template.yml"
    fi
fi

echo ">>> Iniciando OTEL Collector desde /usr/bin/otelcol-contrib..."
/usr/bin/otelcol-contrib --config=$CONFIG_FILE &

sleep 5

echo ">>> Iniciando Aplicación Java..."
java -jar app.jar