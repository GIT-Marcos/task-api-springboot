package com.usuario.todolist.documentation;

import com.usuario.todolist.dto.request.*;
import com.usuario.todolist.dto.response.TaskResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Window;
import org.springframework.http.ResponseEntity;

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
            description = "Actualiza TOTALMENTE los datos de una tarea existente por su identificador único."
    )
    @ApiResponse(responseCode = "200", description = "Tarea actualizada correctamente")
    ResponseEntity<TaskResponse> updateTask(
            @Parameter(description = "ID numérico único de la tarea", example = "42", required = true)
            Long id,

            @Parameter(description = "Datos a actualizar de la tarea", required = true)
            TaskUpdateRequest tDTO
    );

    @Operation(
            summary = "Actualizar una tarea existente",
            description = "Actualiza PARCIAL o totalmente los datos de una tarea existente por su identificador único."
    )
    @ApiResponse(responseCode = "200", description = "Tarea actualizada correctamente")
    ResponseEntity<TaskResponse> patchTask(
            @Parameter(description = "ID numérico único de la tarea", example = "42")
            Long id,

            @Parameter(description = "Datos a actualizar de la tarea")
            TaskPatchRequest tDTO
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
            summary = "Ver todas o buscar tareas con filtros",
            description = """
                    Permite buscar tareas utilizando Keyset Pagination (basado en cursores).
                    Todos los filtros son opcionales. El cursor se compone de 'lastId' y 'lastDate'.
                    """
    )
    @ApiResponse(responseCode = "200", description = "Listado de tareas que coinciden con los filtros")
    ResponseEntity<Window<TaskResponse>> find(
            @ParameterObject TaskFilterRequest filter,
            @ParameterObject CursorPageRequest page
    );
}