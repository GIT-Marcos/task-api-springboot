package com.usuario.todolist.util;

import com.usuario.todolist.dto.TaskCreateRequest;
import com.usuario.todolist.dto.TaskResponse;
import com.usuario.todolist.dto.TaskUpdateRequest;
import com.usuario.todolist.entity.Task;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TaskMapper {

    public Task toEntity(TaskCreateRequest dto) {
        if (dto == null) return null;

        return new Task(dto.description());
    }

    public Task updateEntityFromDTO(TaskUpdateRequest dto, Task managedTask) {
        if (dto == null || managedTask == null) return null;

        managedTask.setCompleted(dto.completed());
        managedTask.setDescription(dto.description());

        return managedTask;
    }

    public TaskResponse toResponseDTO(Task task) {
        if (task == null) return null;

        return new TaskResponse(task);
    }

    public List<TaskResponse> toResponseDTO(List<Task> tasks) {
        if (tasks == null) tasks = new ArrayList<>();

        return tasks.stream().map(this::toResponseDTO).toList();
    }
}
