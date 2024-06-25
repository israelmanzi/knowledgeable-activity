package com.bank_system.dtos.role;

import com.bank_system.enumerations.user.EUserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CreateRoleDTO {
    @Schema(example = "ADMIN", description = "Role name")
//    @NotBlank(message = "Role name is required")
    private EUserRole name;
}
