package com.clt.toscana.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

/**
 * Common audit columns inherited by every JPA {@code @Entity} in this project.
 *
 * <p>JPA Auditing is activated by
 * {@link com.clt.toscana.config.JpaAuditingConfig @EnableJpaAuditing}.
 * Fields are populated automatically by Spring Data:
 * <ul>
 *   <li>{@code createdAt}  — set once on INSERT.</li>
 *   <li>{@code updatedAt}  — updated on every MERGE.</li>
 *   <li>{@code createdBy}  — requires an {@code AuditorAware<String>} bean
 *                            (added when Spring Security is configured).</li>
 * </ul>
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /**
     * Identity of the actor who created this record.
     * Populated automatically from {@code AuditorAware<String>} once Spring Security is set up.
     */
    @CreatedBy
    @Column(name = "created_by", length = 100, updatable = false)
    private String createdBy;
}
