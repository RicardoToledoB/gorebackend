package com.sistemas.dtos;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterHistoryDTO {
    private Integer id;
    private RegisterDTO register;
    private UserDTO user;
    private String action; // "CREATE", "UPDATE", "DELETE", "RESTORE"
    private String jsonBefore;
    private String jsonAfter;
    private String jsonDiff; // ej: {"n_inventary":{"old":"1001","new":"1002"}, ... }
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
