package com.usuario.todolist.config;

import io.micrometer.core.instrument.Clock;
import io.micrometer.registry.otlp.OtlpConfig;
import io.micrometer.registry.otlp.OtlpMeterRegistry;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class OtelConfig {

    private final String url;
    private final Duration step;

    public OtelConfig(
            @Value("${app.otel.metrics.url:http://localhost:4318/v1/metrics}") String url,
            @Value("${app.otel.metrics.export-step:30s}") Duration step) {
        this.url = url;
        this.step = step;
    }

    @Bean
    public OtlpMeterRegistry otlpMeterRegistry() {
        OtlpConfig otlpConfig = new OtlpConfig() {
            @Override
            public @NonNull String url() {
                return url;
            }

            @Override
            public @NonNull Duration step() {
                return step;
            }

            @Override
            public String get(@NonNull String key) {
                return null;
            }
        };
        return new OtlpMeterRegistry(otlpConfig, Clock.SYSTEM);
    }
}