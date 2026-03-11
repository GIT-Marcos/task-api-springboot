package unit;

import com.usuario.todolist.dto.request.TaskCreateRequest;
import com.usuario.todolist.dto.request.TaskFilterRequest;
import com.usuario.todolist.dto.response.TaskResponse;
import com.usuario.todolist.dto.request.TaskUpdateRequest;
import com.usuario.todolist.entity.Task;
import com.usuario.todolist.exception.DuplicatedTaskException;
import com.usuario.todolist.exception.TaskNotFoundException;
import com.usuario.todolist.repository.TaskRepository;
import com.usuario.todolist.service.TaskService;
import com.usuario.todolist.util.TaskMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository repo;

    @Mock
    private TaskMapper mapper;

    @InjectMocks
    private TaskService taskService;

    // =========== TESTS PARA SAVE ===========

    @Test
    void save_ValidTask_ReturnsSavedTaskDTO() {
        // Preparar
        String description = "Testear";
        TaskCreateRequest createDTO = new TaskCreateRequest(description);
        Task entityToSave = new Task(description);
        Task savedEntity = new Task(description);
        savedEntity.setId(1L);
        TaskResponse expectedResponse = new TaskResponse(1L, description, false, LocalDateTime.now());

        // Comportar
        when(repo.existsByDescription(description)).thenReturn(false);
        when(mapper.toEntity(createDTO)).thenReturn(entityToSave);
        when(repo.save(entityToSave)).thenReturn(savedEntity);
        when(mapper.toResponseDTO(savedEntity)).thenReturn(expectedResponse);

        // Ejecutar
        TaskResponse actualResponse = taskService.save(createDTO);

        // Asegurar
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.id()).isEqualTo(1L);
        assertThat(actualResponse.description()).isEqualTo(description);

        // Verificar
        verify(repo, times(1)).save(entityToSave);
    }

    @Test
    void save_DuplicatedDescription_ThrowsDuplicateTaskException() {
        // Preparar
        String duplicatedDescription = "Tarea Duplicada";
        TaskCreateRequest createDTO = new TaskCreateRequest(duplicatedDescription);

        // Comportar
        when(repo.existsByDescription(duplicatedDescription)).thenReturn(true);

        // Ejecutar y asegurar
        assertThatThrownBy(() -> taskService.save(createDTO))
                .isInstanceOf(DuplicatedTaskException.class);

        // Verificar
        verify(repo, never()).save(any());
    }

    /**
     * Race condition.
     */
    @Test
    void save_DataIntegrityViolation_ThrowsDuplicatedTaskException() {
        // Preparar
        String description = "Tarea concurrente";
        TaskCreateRequest createDTO = new TaskCreateRequest(description);
        Task entityToSave = new Task(description);

        // Comportar
        when(repo.existsByDescription(description)).thenReturn(false);
        when(mapper.toEntity(createDTO)).thenReturn(entityToSave);
        when(repo.save(entityToSave)).thenThrow(new DataIntegrityViolationException("Duplicate task"));

        // Ejecutar y asegurar
        assertThatThrownBy(() -> taskService.save(createDTO))
                .isInstanceOf(DuplicatedTaskException.class);

        // Verificar
        verify(repo, times(1)).save(entityToSave);
    }

    // ========================= UPDATE ============================

    @Test
    void update_ValidTask_ReturnsUpdatedTaskDTO() {
        // Preparar
        Long id = 1L;
        String newDescription = "Tarea actualizada";
        TaskUpdateRequest updateDTO = new TaskUpdateRequest(newDescription, true);
        Task managedTask = new Task("Tarea original");
        managedTask.setId(id);
        Task updatedTask = new Task(newDescription);
        updatedTask.setId(id);
        updatedTask.setCompleted(true);
        TaskResponse expectedResponse = new TaskResponse(id, newDescription, true, LocalDateTime.now());

        // Comportar
        when(repo.findById(id)).thenReturn(Optional.of(managedTask));

        // CAMBIO AQUÍ: Ahora se usa existsByDescriptionAndIdNot
        when(repo.existsByDescriptionAndIdNot(newDescription, id)).thenReturn(false);

        when(mapper.updateEntityFromDTO(updateDTO, managedTask)).thenReturn(updatedTask);
        when(repo.save(updatedTask)).thenReturn(updatedTask);
        when(mapper.toResponseDTO(updatedTask)).thenReturn(expectedResponse);

        // Ejecutar
        TaskResponse actualResponse = taskService.update(id, updateDTO);

        // Asegurar
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.id()).isEqualTo(id);
        assertThat(actualResponse.description()).isEqualTo(newDescription);
        assertThat(actualResponse.completed()).isTrue();

        // Verificar
        verify(repo, times(1)).save(updatedTask);
    }

    @Test
    void update_TaskNotFound_ThrowsTaskNotFoundException() {
        // Preparar
        Long id = 99L;
        TaskUpdateRequest updateDTO = new TaskUpdateRequest("Cualquier cosa", false);

        // Comportar
        when(repo.findById(id)).thenReturn(Optional.empty());

        // Ejecutar y asegurar
        assertThatThrownBy(() -> taskService.update(id, updateDTO))
                .isInstanceOf(TaskNotFoundException.class);

        // Verificar
        verify(repo, never()).save(any());
    }

    @Test
    void update_DuplicatedDescription_ThrowsDuplicatedTaskException() {
        // Preparar
        Long id = 1L;
        String duplicatedDescription = "Tarea Duplicada";
        TaskUpdateRequest updateDTO = new TaskUpdateRequest(duplicatedDescription, false);
        Task managedTask = new Task("Tarea original");
        managedTask.setId(id);

        // Comportar
        when(repo.findById(id)).thenReturn(Optional.of(managedTask));

        when(repo.existsByDescriptionAndIdNot(duplicatedDescription, id)).thenReturn(true);

        // Ejecutar y asegurar
        assertThatThrownBy(() -> taskService.update(id, updateDTO))
                .isInstanceOf(DuplicatedTaskException.class);

        // Verificar
        verify(repo, never()).save(any());
    }

    /**
     * Race condition.
     */
    @Test
    void update_DataIntegrityViolation_ThrowsDuplicatedTaskException() {
        // Preparar
        Long id = 1L;
        String description = "Tarea concurrente";
        TaskUpdateRequest updateDTO = new TaskUpdateRequest(description, false);
        Task managedTask = new Task("Tarea original");
        managedTask.setId(id);

        // Comportar
        when(repo.findById(id)).thenReturn(Optional.of(managedTask));

        when(repo.existsByDescriptionAndIdNot(description, id)).thenReturn(false);

        when(mapper.updateEntityFromDTO(updateDTO, managedTask)).thenReturn(managedTask);
        when(repo.save(managedTask)).thenThrow(new DataIntegrityViolationException("Duplicate"));

        // Ejecutar y asegurar
        assertThatThrownBy(() -> taskService.update(id, updateDTO))
                .isInstanceOf(DuplicatedTaskException.class);

        // Verificar
        verify(repo, times(1)).save(managedTask);
    }

// ========================= DELETE ============================

    @Test
    void delete_ExistingTask_DeletesSuccessfully() {
        // Preparar
        Long id = 1L;

        // Comportar
        when(repo.existsById(id)).thenReturn(true);

        // Ejecutar
        taskService.delete(id);

        // Verificar
        verify(repo, times(1)).deleteById(id);
    }

    @Test
    void delete_TaskNotFound_ThrowsTaskNotFoundException() {
        // Preparar
        Long id = 99L;

        // Comportar
        when(repo.existsById(id)).thenReturn(false);

        // Ejecutar y asegurar
        assertThatThrownBy(() -> taskService.delete(id))
                .isInstanceOf(TaskNotFoundException.class);

        // Verificar
        verify(repo, never()).deleteById(any());
    }

// ========================= FIND ALL ============================

    @Test
    void findAll_TasksExist_ReturnsTaskList() {
        // Preparar
        Task task1 = new Task("Tarea 1");
        task1.setId(1L);
        Task task2 = new Task("Tarea 2");
        task2.setId(2L);
        List<Task> tasks = List.of(task1, task2);
        List<TaskResponse> expectedResponse = List.of(
                new TaskResponse(1L, "Tarea 1", false, LocalDateTime.now()),
                new TaskResponse(2L, "Tarea 2", false, LocalDateTime.now())
        );

        // Comportar
        when(repo.findAll()).thenReturn(tasks);
        when(mapper.toResponseDTO(tasks)).thenReturn(expectedResponse);

        // Ejecutar
        List<TaskResponse> actualResponse = taskService.findAll();

        // Asegurar
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse).hasSize(2);

        // Verificar
        verify(repo, times(1)).findAll();
    }

    @Test
    void findAll_NoTasks_ReturnsEmptyList() {
        // Preparar
        List<Task> emptyList = List.of();
        List<TaskResponse> expectedResponse = List.of();

        // Comportar
        when(repo.findAll()).thenReturn(emptyList);
        when(mapper.toResponseDTO(emptyList)).thenReturn(expectedResponse);

        // Ejecutar
        List<TaskResponse> actualResponse = taskService.findAll();

        // Asegurar
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse).isEmpty();

        // Verificar
        verify(repo, times(1)).findAll();
    }

// ========================= FIND BY FILTERS ============================

    @Test
    void findByFilters_WithAllFilters_ReturnsFilteredTasks() {
        // Arrange
        TaskFilterRequest filter = new TaskFilterRequest("Tarea",
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31),
                true);

        Task task = new Task("Tarea 1");
        task.setId(1L);
        List<Task> tasks = List.of(task);

        List<TaskResponse> expected = List.of(
                new TaskResponse(1L, "Tarea 1", true, LocalDateTime.now())
        );

        when(repo.findAll(ArgumentMatchers.<Specification<Task>>any())).thenReturn(tasks);
        when(mapper.toResponseDTO(tasks)).thenReturn(expected);

        // Act
        List<TaskResponse> result = taskService.findByFilters(filter);

        // Assert
        assertThat(result).isNotNull().hasSize(1).isEqualTo(expected);
        verify(repo).findAll(ArgumentMatchers.<Specification<Task>>any());
        verify(mapper).toResponseDTO(tasks);
    }

    @Test
    void findByFilters_WithNoMatches_ReturnsEmptyList() {
        // Arrange
        TaskFilterRequest filter = new TaskFilterRequest("Inexistente", (LocalDateTime) null, null, null);
        List<Task> emptyTasks = List.of();
        List<TaskResponse> emptyResponse = List.of();

        when(repo.findAll(ArgumentMatchers.<Specification<Task>>any())).thenReturn(emptyTasks);
        when(mapper.toResponseDTO(emptyTasks)).thenReturn(emptyResponse);

        // Act
        List<TaskResponse> result = taskService.findByFilters(filter);

        // Assert
        assertThat(result).isNotNull().isEmpty();
        verify(repo).findAll(ArgumentMatchers.<Specification<Task>>any());
        verify(mapper).toResponseDTO(emptyTasks);
    }

    @Test
    void findByFilters_WithNullFilters_DelegatesToRepoAndMapper() {
        // Arrange
        TaskFilterRequest filter = new TaskFilterRequest(null, (LocalDateTime) null, null, null);

        Task task1 = new Task("Tarea 1");
        task1.setId(1L);
        Task task2 = new Task("Tarea 2");
        task2.setId(2L);
        List<Task> tasks = List.of(task1, task2);

        List<TaskResponse> expected = List.of(
                new TaskResponse(1L, "Tarea 1", false, LocalDateTime.now()),
                new TaskResponse(2L, "Tarea 2", false, LocalDateTime.now())
        );

        when(repo.findAll(ArgumentMatchers.<Specification<Task>>any())).thenReturn(tasks);
        when(mapper.toResponseDTO(tasks)).thenReturn(expected);

        // Act
        List<TaskResponse> result = taskService.findByFilters(filter);

        // Assert
        assertThat(result).isNotNull().hasSize(2);
        verify(repo).findAll(ArgumentMatchers.<Specification<Task>>any());
        verify(mapper).toResponseDTO(tasks);
    }

    @Test
    void findByFilters_WithPartialFilters_ReturnsMatchingTasks() {
        // Arrange
        TaskFilterRequest filter = new TaskFilterRequest("Tarea", (LocalDateTime) null, null, false);

        Task task = new Task("Tarea importante");
        task.setId(1L);
        List<Task> tasks = List.of(task);

        List<TaskResponse> expected = List.of(
                new TaskResponse(1L, "Tarea importante", false, LocalDateTime.now())
        );

        when(repo.findAll(ArgumentMatchers.<Specification<Task>>any())).thenReturn(tasks);
        when(mapper.toResponseDTO(tasks)).thenReturn(expected);

        // Act
        List<TaskResponse> result = taskService.findByFilters(filter);

        // Assert
        assertThat(result).isNotNull().hasSize(1).isEqualTo(expected);
        verify(repo).findAll(ArgumentMatchers.<Specification<Task>>any());
        verify(mapper).toResponseDTO(tasks);
    }
}