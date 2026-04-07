package com.usuario.todolist.service;

import com.usuario.todolist.dto.request.*;
import com.usuario.todolist.specification.TaskSpecification;
import com.usuario.todolist.exception.DuplicatedTaskException;
import com.usuario.todolist.util.TaskMapper;
import com.usuario.todolist.dto.response.TaskResponse;
import com.usuario.todolist.entity.Task;
import com.usuario.todolist.exception.TaskNotFoundException;
import com.usuario.todolist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskService {

    private final TaskRepository repo;
    private final TaskMapper mapper;

    @Autowired
    public TaskService(TaskRepository repo, TaskMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public TaskResponse save(TaskCreateRequest dto) {
        validateDescriptionUnicityForCreate(dto.description());

        try {
            Task task = mapper.toEntity(dto);
            task = repo.save(task);
            return mapper.toResponseDTO(task);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicatedTaskException(dto.description());
        }
    }

    public TaskResponse update(Long id, TaskUpdateRequest tDTO) {
        Task managedTask = findById(id);

        validateDescriptionUnicityForUpdate(tDTO.description(), id);

        try {
            managedTask = mapper.updateEntityFromDTO(tDTO, managedTask);
            managedTask = repo.save(managedTask);

            return mapper.toResponseDTO(managedTask);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicatedTaskException(tDTO.description());
        }
    }

    public TaskResponse patch(Long id, TaskPatchRequest tDTO) {
        Task managedTask = findById(id);

        try {
            managedTask = mapper.patchEntityFromDTO(tDTO, managedTask);
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

    @Deprecated
    public List<TaskResponse> findAll() {
        return mapper.toResponseDTO(repo.findAll(Sort.by("date").descending()));
    }

    public List<TaskResponse> findByFilters(TaskFilterRequest filter) {
        Specification<Task> spec = TaskSpecification.buildFilter(filter);
        return mapper.toResponseDTO(repo.findAll(spec));
    }

    public Window<TaskResponse> findByFilters(TaskFilterRequest filter, CursorPageRequest cpr) {
        Sort sort = buildSort(cpr.direction());
        final ScrollPosition finalPos = buildScrollPosition(cpr);

        return repo.findBy(
                TaskSpecification.buildFilter(filter),
                query -> query
                        .limit(cpr.size())
                        .sortBy(sort)
                        .scroll(finalPos)
        ).map(TaskResponse::new);
    }

    // ========================= PRIVADOS ============================

    private Sort buildSort(Sort.Direction direction) {
        return Sort.by(direction, "date").and(Sort.by(direction, "id"));
    }

    private ScrollPosition buildScrollPosition(CursorPageRequest cpr) {
        if (cpr.lastId() == null || cpr.lastDate() == null) {
            return ScrollPosition.keyset();
        }

        Map<String, Object> keys = new LinkedHashMap<>();
        keys.put("date", cpr.lastDate());
        keys.put("id", cpr.lastId());

        return ScrollPosition.of(keys, ScrollPosition.Direction.FORWARD);
    }

    private Task findById(Long id) {
        return repo.findById(id).orElseThrow(
                () -> new TaskNotFoundException(id)
        );
    }

    private void validateDescriptionUnicityForCreate(String desc) {
        if (repo.existsByDescriptionIgnoreCase(desc)) {
            throw new DuplicatedTaskException(desc);
        }
    }

    private void validateDescriptionUnicityForUpdate(String desc, Long id) {
        if (repo.existsByDescriptionIgnoreCaseAndIdNot(desc, id)) {
            throw new DuplicatedTaskException(desc);
        }
    }
}
