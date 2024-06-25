package com.bank_system.dtos.role;

import com.bank_system.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
public class RolesResponseDTO {
    private Page<Role> roles;
}
