package com.bank_system.dtos.user;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Data
@RequiredArgsConstructor
public class UserRoleModificationDTO {
    private Set<Long> roles;
}
