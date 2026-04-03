#!/bin/sh
if [ "$APP_PROFILE" = "prod" ]; then
    CONFIG_FILE="/app/otel-config-prod.yml"
else
    # En DEV, si no renombraste el template, intentamos usar el template
    if [ -f "/app/otel-config.yml" ]; then
        CONFIG_FILE="/app/otel-config.yml"
    else
        CONFIG_FILE="/app/otel-config-dev.template.yml"
    fi
fi

echo "Uso de configuración: $CONFIG_FILE"
/app/otelcol --config=$CONFIG_FILE &
java -jar app.jar