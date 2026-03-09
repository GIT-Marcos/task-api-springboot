package com.usuario.todolist.documentation;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "Error de validación de los datos de entrada"),
        @ApiResponse(responseCode = "404", description = "Tarea no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
})
public @interface ApiStandardResponse {
}
