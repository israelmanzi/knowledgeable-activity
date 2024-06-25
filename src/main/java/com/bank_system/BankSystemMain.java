package com.bank_system;

import com.bank_system.dtos.user.CreateUserDTO;
import com.bank_system.enumerations.user.EUserRole;
import com.bank_system.models.Role;
import com.bank_system.models.User;
import com.bank_system.repositories.IUserRepository;
import com.bank_system.services.interfaces.RoleService;
import com.bank_system.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@SpringBootApplication
@BankSystemApplication
@EnableCaching
@RequiredArgsConstructor
public class BankSystemMain extends SpringBootServletInitializer {
    private final RoleService roleService;
    private final UserService userService;
    private final IUserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(BankSystemMain.class, args);
    }

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.firstName}")
    private String adminFirstName;

    @Value("${admin.lastName}")
    private String adminLastName;

    @Bean
    public void seedData() {
        Set<EUserRole> userRoleSet = new HashSet<>();
        userRoleSet.add(EUserRole.ADMIN);
        userRoleSet.add(EUserRole.CUSTOMER);
        for (EUserRole role : userRoleSet) {
            if (!this.roleService.isRolePresent(role)) {
                this.roleService.createRole(role);
            }
        }

        Optional<User> isUserPresent = userRepository.findUserByEmail(adminEmail);
        if (isUserPresent.isEmpty()) {
            CreateUserDTO createUserDTO = new CreateUserDTO(adminEmail, adminUsername, adminPassword, adminFirstName, adminLastName);
            User userEntity = this.userService.createUserEntity(createUserDTO);
            Role role = this.roleService.getRoleByName(EUserRole.ADMIN);
            userEntity.getRoles().add(role);
            userRepository.save(userEntity);
        }
    }
}
