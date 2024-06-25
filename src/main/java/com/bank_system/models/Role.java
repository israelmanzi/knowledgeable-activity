package com.bank_system.models;

import com.bank_system.enumerations.user.EUserRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role extends Base {
    @Enumerated(EnumType.STRING)
    @Column(name = "name", unique = true, nullable = false)
    private EUserRole name;
}
