package com.bank_system.services.interfaces;

import com.bank_system.dtos.response.ApiResponse;
import com.bank_system.dtos.user.*;
import com.bank_system.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface UserService {
    public User createUserEntity(CreateUserDTO createUserDTO);

    public User findUserById(Long userId);

    public User getLoggedInUser();

    public ResponseEntity<ApiResponse<UserResponseDTO>> createUser(CreateUserDTO createUserDTO);

    public ResponseEntity<ApiResponse<UsersResponseDTO>> getUsers(Pageable pageable);

    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(Long id);

    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(Long userId, UpdateUserDTO updateUserDTO);

    public ResponseEntity<ApiResponse<UserResponseDTO>> addRoles(Long userId, UserRoleModificationDTO userRoleModificationDTO);

    public ResponseEntity<ApiResponse<UserResponseDTO>> removeRoles(Long userId, UserRoleModificationDTO userRoleModificationDTO);

    public ResponseEntity<ApiResponse<Object>> deleteUser(Long userId);
}
