package com.bank_system.dtos.role;

import com.bank_system.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoleResponseDTO {
    private Role role;
}
