package com.usuario.todolist.config;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.config.MeterFilter;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.Arrays;

@Configuration(proxyBeanMethods = false)
public class MicrometerConfig {

    private final String appName;
    private final String profile;
    private final int allowableTags;
    private static final String HTTP_REQUESTS = "http.server.requests";

    public MicrometerConfig(@Value("${spring.application.name:task-api}") String appName,
                            @Value("${spring.profiles.active:dev}") String profile,
                            @Value("${management.metrics.limits.uri:100}") int allowableTags) {
        this.appName = appName;
        this.profile = profile;
        this.allowableTags = allowableTags;
    }

    @Bean
    @Order(1)
    public MeterFilter commonTags() {
        return MeterFilter.commonTags(Arrays.asList(
                Tag.of("env", profile),
                Tag.of("app", appName)
        ));
    }

    /**
     * Metrics firewall - denegation filter
     */
    @Bean
    @Order(2)
    public MeterFilter noiseDenyFilter() {
        return MeterFilter.deny(id -> {
            String uri = id.getTag("uri");
            return uri != null && (uri.contains("favicon") || uri.equals("UNKNOWN"));
        });
    }

    /**
     * Cardinality protection of TSDB.
     */
    @Bean
    @Order(3)
    public MeterFilter proximityTransformationFilter() {
        return new MeterFilter() {
            @Override
            public Meter.@NonNull Id map(Meter.@NonNull Id id) {
                if (!id.getName().equals(HTTP_REQUESTS)) {
                    return id;
                }

                String uri = id.getTag("uri");
                String status = id.getTag("status");

                if (uri != null && uri.startsWith("/actuator")) {
                    return id.withTag(Tag.of("uri", "/actuator/**"));
                }

                if ("404".equals(status)) {
                    return id.withTag(Tag.of("uri", "NOT_FOUND"));
                }

                return id;
            }
        };
    }

    /**
     * OOM protection
     */
    @Bean
    @Order(4)
    public MeterFilter limitCardinalityFilter() {
        return MeterFilter.maximumAllowableTags(
                "http.server.requests",
                "uri",
                allowableTags,
                new MeterFilter() {
                    @Override
                    public Meter.@NonNull Id map(Meter.@NonNull Id id) {
                        return id.withTag(Tag.of("uri", "REACHED_LIMIT"));
                    }
                }
        );
    }
}