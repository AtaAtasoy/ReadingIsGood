package com.ataatasoy.readingisgood.repository;

import com.ataatasoy.readingisgood.models.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserAccountRepository extends JpaRepository<UserAccount, UUID> {
}
