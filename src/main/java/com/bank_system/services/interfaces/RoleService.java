package com.bank_system.services.interfaces;

import com.bank_system.dtos.response.ApiResponse;
import com.bank_system.dtos.role.CreateRoleDTO;
import com.bank_system.dtos.role.RoleResponseDTO;
import com.bank_system.dtos.role.RolesResponseDTO;
import com.bank_system.enumerations.user.EUserRole;
import com.bank_system.models.Role;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface RoleService {
    public Role getRoleById(Long roleId);

    public Role getRoleByName(EUserRole roleName);

    public void createRole(EUserRole roleName);

    public ResponseEntity<ApiResponse<RoleResponseDTO>> createRole(CreateRoleDTO createRoleDTO);

    public ResponseEntity<ApiResponse<RolesResponseDTO>> getRoles(Pageable pageable);

    public Role deleteRole(Long roleId);

    public boolean isRolePresent(EUserRole roleName);
}
