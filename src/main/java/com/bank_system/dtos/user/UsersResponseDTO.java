package com.bank_system.dtos.user;

import com.bank_system.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
public class UsersResponseDTO {
    Page<User> users;
}
