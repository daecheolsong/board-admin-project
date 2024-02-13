package com.example.boardadminproject.repository;

import com.example.boardadminproject.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author daecheol song
 * @since 1.0
 */
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    UserAccount findByUserId(String userId);

}

