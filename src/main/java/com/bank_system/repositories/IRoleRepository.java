package com.bank_system.repositories;

import com.bank_system.enumerations.user.EUserRole;
import com.bank_system.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findRoleByName(EUserRole name);
}
