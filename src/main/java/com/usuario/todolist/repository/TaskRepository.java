package com.usuario.todolist.repository;

import com.usuario.todolist.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    Boolean existsByDescriptionIgnoreCase(String description);

    Boolean existsByDescriptionIgnoreCaseAndIdNot(String description, Long id);
}
