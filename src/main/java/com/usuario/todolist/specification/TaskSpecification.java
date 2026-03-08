package com.usuario.todolist.specification;

import com.usuario.todolist.dto.TaskFilterDTO;
import com.usuario.todolist.entity.Task;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TaskSpecification {

    public static Specification<Task> buildFilter(TaskFilterDTO filter) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();
            // todo: ver si usar Metamodel de JPA para evitar String directos
            if (filter.description() != null && !filter.description().isEmpty()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("description")),
                                "%" + filter.description().toLowerCase() + "%"
                        )
                );
            }

            if (filter.minDate() != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("date"),
                                filter.minDate()
                        )
                );
            }

            if (filter.maxDate() != null) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("date"),
                                filter.maxDate()
                        )
                );
            }

            if (filter.completed() != null) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("completed"),
                                filter.completed()
                        )
                );
            }

            query.orderBy(
                    criteriaBuilder.desc(root.get("date"))
            );

            if (predicates.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}