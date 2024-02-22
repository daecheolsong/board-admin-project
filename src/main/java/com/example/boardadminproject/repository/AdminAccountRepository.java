package com.example.boardadminproject.repository;

import com.example.boardadminproject.domain.AdminAccount;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author daecheol song
 * @since 1.0
 */
public interface AdminAccountRepository extends JpaRepository<AdminAccount, Long> {

    AdminAccount findByUserId(String userId);

    void deleteByUserId(String userId);

}

