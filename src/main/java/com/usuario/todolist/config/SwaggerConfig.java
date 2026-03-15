package com.usuario.todolist.config;

import com.usuario.todolist.dto.response.ErrorResponse;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI custonOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("TASK API")
                        .version("1.0")
                )
                .components(new Components().addSchemas("ErrorResponse", new Schema<>().jsonSchemaImpl(ErrorResponse.class)));
    }

}
