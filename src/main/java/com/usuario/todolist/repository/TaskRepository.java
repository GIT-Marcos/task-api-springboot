package com.usuario.todolist.repository;

import com.usuario.todolist.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByCompleted(Boolean isCompleted);

    @Query("""
        SELECT t FROM Task t
        WHERE (:desc IS NULL OR LOWER(t.description) LIKE LOWER(CONCAT('%', :desc, '%')))
          AND (:minDate IS NULL OR t.date >= :minDate)
          AND (:maxDate IS NULL OR t.date <= :maxDate)
          AND (:state IS NULL OR t.completed = :state)
        ORDER BY t.date DESC
    """)
    List<Task> findByFilters(@Param("desc") String desc,
                             @Param("minDate") LocalDateTime minDate,
                             @Param("maxDate") LocalDateTime maxDate,
                             @Param("state") Boolean completed);

    Boolean existsByDescription(String description);

}
