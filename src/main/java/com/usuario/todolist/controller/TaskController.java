package com.usuario.todolist.controller;

import com.usuario.todolist.documentation.TaskApiDoc;
import com.usuario.todolist.dto.request.*;
import com.usuario.todolist.dto.response.TaskResponse;
import com.usuario.todolist.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Window;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<Window<TaskResponse>> find(TaskFilterRequest filter, CursorPageRequest cpr) {
        Window<TaskResponse> taskWindow = serv.findByFilters(filter, cpr);
        return ResponseEntity.ok(taskWindow);
    }
}
