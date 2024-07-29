package com.unsia.edu.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "m_admin")
public class Admin extends Auditable {

    @Column(name = "email", unique = true)
    private String email;
    @OneToOne
    private EntityCredential credential;
}
