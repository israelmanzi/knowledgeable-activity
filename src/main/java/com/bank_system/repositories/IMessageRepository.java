package com.bank_system.repositories;

import com.bank_system.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMessageRepository extends JpaRepository<Message, Long> {
}
