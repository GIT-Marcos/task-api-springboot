package com.usuario.todolist.specification;

import com.usuario.todolist.dto.request.TaskFilterRequest;
import com.usuario.todolist.entity.Task;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TaskSpecification {

    public static Specification<Task> buildFilter(TaskFilterRequest filter) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filter.description() != null && !filter.description().isBlank()) {
                String pattern = "%" + filter.description().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("description")), pattern));
            }

            if (filter.minDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("date"), filter.minDate()));
            }
            if (filter.maxDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("date"), filter.maxDate()));
            }

            if (filter.completed() != null) {
                predicates.add(cb.equal(root.get("completed"), filter.completed()));
            }

            if (predicates.isEmpty()) {
                return cb.conjunction();
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}