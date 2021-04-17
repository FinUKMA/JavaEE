package com.kma.specification.specification;

import java.time.LocalDateTime;

import javax.persistence.criteria.JoinType;

import org.springframework.data.jpa.domain.Specification;

import com.kma.specification.domain.entities.BookEntity;
import com.kma.specification.domain.type.BookStatus;

public class BookSpecificationFactory {

    public static Specification<BookEntity> activeOnly() {
        return (root, query, cb) -> cb.equal(root.get("status"), BookStatus.ACTIVE);
    }

    public static Specification<BookEntity> newestOnly(final LocalDateTime createdAfter) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("createdTimestamp"), createdAfter);
    }

    public static Specification<BookEntity> authorBooks(final int authorId) {
        return (root, query, cb) -> cb.equal(root.join("authors").get("id"), authorId);
    }

    public static Specification<BookEntity> sortByCategoryId() {
        return (root, query, cb) -> {
            query.orderBy(cb.asc(root.join("category").get("id")));
            return cb.conjunction();
        };
    }

    public static Specification<BookEntity> fetchAll() {
        return (root, query, cb) -> {
            if (!query.getResultType().equals(Long.class)) {
                root.fetch("category");
                root.fetch("authors", JoinType.LEFT);
            }
            return cb.conjunction();
        };
    }

}
