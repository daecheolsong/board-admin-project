package com.example.boardadminproject.service;

import com.example.boardadminproject.domain.AdminAccount;
import com.example.boardadminproject.domain.constant.RoleType;
import com.example.boardadminproject.dto.AdminAccountDto;
import com.example.boardadminproject.repository.AdminAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author daecheol song
 * @since 1.0
 */

@Transactional
@RequiredArgsConstructor
@Service
public class AdminAccountService {

    private final AdminAccountRepository adminAccountRepository;

    @Transactional(readOnly = true)
    public Optional<AdminAccountDto> findUser(String username) {

        return Optional.ofNullable(adminAccountRepository.findByUserId(username))
                .map(AdminAccountDto::from);
    }

    public AdminAccountDto saveUser(String userId, String userPassword, Set<RoleType> roleTypes, String email, String nickname, String memo) {

        return AdminAccountDto.from(
                adminAccountRepository.save(AdminAccount.of(userId, userPassword, roleTypes, email, nickname, memo, userId))
        );
    }

    @Transactional(readOnly = true)
    public List<AdminAccountDto> users() {
        return adminAccountRepository.findAll().stream()
                .map(AdminAccountDto::from)
                .toList();
    }

    public void deleteUser(String username) {
        adminAccountRepository.deleteByUserId(username);
    }

}
