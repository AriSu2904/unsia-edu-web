package com.unsia.edu.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;
    @CreatedDate
    protected LocalDateTime createdAt;
    @LastModifiedDate
    protected LocalDateTime updatedAt;
}
