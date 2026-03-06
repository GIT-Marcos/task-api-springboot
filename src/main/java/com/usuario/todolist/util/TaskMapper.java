package com.usuario.todolist.util;

import com.usuario.todolist.dto.TaskCreateDTO;
import com.usuario.todolist.dto.TaskResponseDTO;
import com.usuario.todolist.dto.TaskUpdateDTO;
import com.usuario.todolist.entity.Task;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TaskMapper {

    public Task toEntity(TaskCreateDTO dto) {
        if (dto == null) return null;

        return new Task(dto.description());
    }

    public Task updateEntityFromDTO(TaskUpdateDTO dto, Task managedTask) {
        if (dto == null || managedTask == null) return null;

        managedTask.setCompleted(dto.completed());
        managedTask.setDescription(dto.description());

        return managedTask;
    }

    public TaskResponseDTO toResponseDTO(Task task) {
        if (task == null) return null;

        return new TaskResponseDTO(task);
    }

    public List<TaskResponseDTO> toResponseDTO(List<Task> tasks) {
        if (tasks == null) tasks = new ArrayList<>();

        return tasks.stream().map(this::toResponseDTO).toList();
    }
}
