package com.usuario.todolist.controller;

import com.usuario.todolist.dto.TaskFilterDTO;
import com.usuario.todolist.dto.TaskResponseDTO;
import com.usuario.todolist.dto.TaskCreateDTO;
import com.usuario.todolist.dto.TaskUpdateDTO;
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
public class TaskController {

    private final TaskService serv;

    @Autowired
    public TaskController(TaskService serv) {
        this.serv = serv;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> saveTask(@RequestBody @Valid TaskCreateDTO tDTO) {
        TaskResponseDTO saved = serv.save(tDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id, @RequestBody @Valid TaskUpdateDTO tDTO) {
        TaskResponseDTO updatedTask = serv.update(id, tDTO);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        serv.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ========================= LECTURA ============================

    @GetMapping("/filters")
    public ResponseEntity<List<TaskResponseDTO>> findByFilters(@RequestParam(required = false) String description,
                                                               @RequestParam(required = false) LocalDate minDate,
                                                               @RequestParam(required = false) LocalDate maxDate,
                                                               @RequestParam(required = false) Boolean completed) {
        TaskFilterDTO filter = new TaskFilterDTO(description, minDate, maxDate, completed);
        return ResponseEntity.ok(serv.findByFilters(filter));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> findAll() {
        List<TaskResponseDTO> results = serv.findAll();
        if (results.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(results);
    }
}
