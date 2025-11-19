package com.sistemas.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="registers_histories")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@SQLDelete(sql = "UPDATE registers_histories SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class RegisterHistoryEntity implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "register_id", nullable = true)
    @org.hibernate.annotations.NotFound(action = org.hibernate.annotations.NotFoundAction.IGNORE)
    private RegisterEntity register;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    @org.hibernate.annotations.NotFound(action = org.hibernate.annotations.NotFoundAction.IGNORE)
    private UserEntity user;

    // Tipo de acción: CREACIÓN, EDICIÓN, ELIMINACIÓN, RESTAURACIÓN, etc.
    private String action; // "CREATE", "UPDATE", "DELETE", "RESTORE"

    // Estado completo ANTES del cambio
    @Column(columnDefinition = "TEXT")
    private String jsonBefore;

    // Estado completo DESPUÉS del cambio
    @Column(columnDefinition = "TEXT")
    private String jsonAfter;

    // (Opcional) Solo los campos que cambiaron, para facilitar el front
    @Column(columnDefinition = "TEXT")
    private String jsonDiff; // ej: {"n_inventary":{"old":"1001","new":"1002"}, ... }


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    @PrePersist
    private void createdAt(){
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    private void updatedAt(){
        this.updatedAt = LocalDateTime.now();
    }
}
