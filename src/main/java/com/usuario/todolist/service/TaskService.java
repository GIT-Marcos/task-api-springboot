package com.usuario.todolist.service;

import com.usuario.todolist.TaskSpecification;
import com.usuario.todolist.exception.DuplicatedTaskException;
import com.usuario.todolist.util.TaskMapper;
import com.usuario.todolist.dto.TaskCreateDTO;
import com.usuario.todolist.dto.TaskFilterDTO;
import com.usuario.todolist.dto.TaskResponseDTO;
import com.usuario.todolist.dto.TaskUpdateDTO;
import com.usuario.todolist.entity.Task;
import com.usuario.todolist.exception.TaskNotFoundException;
import com.usuario.todolist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository repo;
    private final TaskMapper mapper;

    @Autowired
    public TaskService(TaskRepository repo, TaskMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public TaskResponseDTO save(TaskCreateDTO dto) {
        validateDescriptionUnicity(dto.description());

        try {
            Task task = mapper.toEntity(dto);
            task = repo.save(task);
            return mapper.toResponseDTO(task);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicatedTaskException(dto.description());
        }
    }

    public TaskResponseDTO update(Long id, TaskUpdateDTO tDTO) {
        Task managedTask = repo.findById(id).orElseThrow(
                () -> new TaskNotFoundException(id)
        );

        validateDescriptionUnicity(tDTO.description());

        try {
            managedTask = mapper.updateEntityFromDTO(tDTO, managedTask);
            managedTask = repo.save(managedTask);

            return mapper.toResponseDTO(managedTask);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicatedTaskException(tDTO.description());
        }
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new TaskNotFoundException(id);
        }

        repo.deleteById(id);
    }

    // ========================= LECTURA ============================

    public List<TaskResponseDTO> findAll() {
        return mapper.toResponseDTO(repo.findAll());
    }

    public List<TaskResponseDTO> findByFilters(TaskFilterDTO filter) {
        Specification<Task> spec = TaskSpecification.buildFilter(filter);
        return mapper.toResponseDTO(repo.findAll(spec));
    }

    // ========================= PRIVADOS ============================

    private void validateDescriptionUnicity(String desc) {
        if (repo.existsByDescription(desc)) {
            throw new DuplicatedTaskException(desc);
        }
    }
}
