package com.usuario.todolist.documentation;

import com.usuario.todolist.dto.request.TaskCreateRequest;
import com.usuario.todolist.dto.response.TaskResponse;
import com.usuario.todolist.dto.request.TaskUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Tasks", description = "API para gestión y administración de tareas del Todo List")
@ApiStandardResponse
public interface TaskApiDoc {

    @Operation(
            summary = "Crear una nueva tarea",
            description = "Crea y persiste una nueva tarea en el sistema. Todos los campos serán validados según las reglas de negocio."
    )
    @ApiResponse(responseCode = "201", description = "Tarea creada correctamente")
    ResponseEntity<TaskResponse> saveTask(
            @Parameter(description = "Datos de la tarea a crear", required = true)
            TaskCreateRequest tDTO
    );


    @Operation(
            summary = "Actualizar una tarea existente",
            description = "Actualiza parcial o totalmente los datos de una tarea existente por su identificador único."
    )
    @ApiResponse(responseCode = "200", description = "Tarea actualizada correctamente")
    ResponseEntity<TaskResponse> updateTask(
            @Parameter(description = "ID numérico único de la tarea", example = "42", required = true)
            Long id,

            @Parameter(description = "Datos a actualizar de la tarea", required = true)
            TaskUpdateRequest tDTO
    );


    @Operation(
            summary = "Eliminar una tarea",
            description = "Elimina permanentemente una tarea del sistema por su ID."
    )
    @ApiResponse(responseCode = "204", description = "Tarea eliminada correctamente, no hay contenido que devolver")
    ResponseEntity<Void> deleteTask(
            @Parameter(description = "ID numérico único de la tarea", example = "42", required = true)
            Long id
    );


    @Operation(
            summary = "Listar todas las tareas",
            description = "Retorna el listado completo de todas las tareas almacenadas en el sistema."
    )
    @ApiResponse(responseCode = "200", description = "Listado de tareas retornado correctamente")
    @ApiResponse(responseCode = "204", description = "No existen tareas registradas en el sistema")
    ResponseEntity<List<TaskResponse>> findAll();


    @Operation(
            summary = "Buscar tareas con filtros",
            description = "Permite buscar y filtrar tareas por diferentes criterios. Todos los filtros son opcionales y se combinan entre si."
    )
    @ApiResponse(responseCode = "200", description = "Listado de tareas que coinciden con los filtros")
    @ApiResponse(responseCode = "204", description = "Ninguna tarea coincide con los filtros indicados")
    ResponseEntity<List<TaskResponse>> findByFilters(

            @Parameter(description = "Filtrar por descripción que contenga el texto", example = "comprar pan")
            String description,

            @Parameter(description = "Filtrar tareas con fecha mayor o igual a la indicada", example = "2026-01-01")
            LocalDate minDate,

            @Parameter(description = "Filtrar tareas con fecha menor o igual a la indicada", example = "2026-12-31")
            LocalDate maxDate,

            @Parameter(description = "Filtrar solo tareas completadas o pendientes", example = "false")
            Boolean completed
    );

}