package org.users.users.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_at", updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "created_by", updatable = false)
    @CreatedBy
    private String createdBy;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    @LastModifiedBy
    private String updatedBy;

    /**
     * Sets last-updated fields on first persist so they are never null.
     * AuditingEntityListener has already set createdAt/createdBy at this point.
     */
    @PrePersist
    private void setInitialLastModified() {
        if (this.updatedAt == null) {
            this.updatedAt = this.createdAt != null ? this.createdAt : LocalDateTime.now();
        }
        if (this.updatedBy == null) {
            this.updatedBy = this.createdBy != null ? this.createdBy : "SYSTEM";
        }
    }

}
