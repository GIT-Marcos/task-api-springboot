package com.usuario.todolist.controller;

import com.usuario.todolist.documentation.TaskApiDoc;
import com.usuario.todolist.dto.request.*;
import com.usuario.todolist.dto.response.TaskResponse;
import com.usuario.todolist.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Window;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/tasks")
public class TaskController implements TaskApiDoc {

    private final TaskService serv;

    @Autowired
    public TaskController(TaskService serv) {
        this.serv = serv;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> saveTask(@RequestBody @Valid TaskCreateRequest tDTO) {
        TaskResponse saved = serv.save(tDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody @Valid TaskUpdateRequest tDTO) {
        TaskResponse updatedTask = serv.update(id, tDTO);
        return ResponseEntity.ok(updatedTask);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponse> patchTask(@PathVariable Long id, @RequestBody @Valid TaskPatchRequest tDTO) {
        TaskResponse patchedTask = serv.patch(id, tDTO);
        return ResponseEntity.ok(patchedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        serv.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ========================= LECTURA ============================

    @GetMapping
    public ResponseEntity<Window<TaskResponse>> find(@RequestParam(required = false) String description,
                                                     @RequestParam(required = false)
                                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime minDate,
                                                     @RequestParam(required = false)
                                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime maxDate,
                                                     @RequestParam(required = false) Boolean completed,
                                                     @RequestParam(required = false) Long lastId,
                                                     @RequestParam(required = false)
                                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastDate,
                                                     @RequestParam(required = false) Integer size,
                                                     @RequestParam(required = false) Sort.Direction direction) {

        TaskFilterRequest filter = new TaskFilterRequest(description, minDate, maxDate, completed);
        CursorPageRequest cpr = new CursorPageRequest(lastId, lastDate, size, direction);

        Window<TaskResponse> taskWindow = serv.findByFilters(filter, cpr);
        return ResponseEntity.ok(taskWindow);
    }
}
