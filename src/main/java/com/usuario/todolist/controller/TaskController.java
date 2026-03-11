package com.usuario.todolist.controller;

import com.usuario.todolist.documentation.TaskApiDoc;
import com.usuario.todolist.dto.request.TaskFilterRequest;
import com.usuario.todolist.dto.response.TaskResponse;
import com.usuario.todolist.dto.request.TaskCreateRequest;
import com.usuario.todolist.dto.request.TaskUpdateRequest;
import com.usuario.todolist.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        serv.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ========================= LECTURA ============================

    @GetMapping("/filters")
    public ResponseEntity<List<TaskResponse>> findByFilters(@RequestParam(required = false) String description,
                                                            @RequestParam(required = false) LocalDate minDate,
                                                            @RequestParam(required = false) LocalDate maxDate,
                                                            @RequestParam(required = false) Boolean completed) {
        TaskFilterRequest filter = new TaskFilterRequest(description, minDate, maxDate, completed);
        return ResponseEntity.ok(serv.findByFilters(filter));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> findAll() {
        List<TaskResponse> results = serv.findAll();
        return ResponseEntity.ok(results);
    }
}
