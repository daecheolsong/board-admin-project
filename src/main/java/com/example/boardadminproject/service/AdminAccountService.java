package com.example.boardadminproject.service;

import com.example.boardadminproject.domain.constant.RoleType;
import com.example.boardadminproject.dto.AdminAccountDto;
import com.example.boardadminproject.repository.AdminAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author daecheol song
 * @since 1.0
 */

@RequiredArgsConstructor
@Service
public class AdminAccountService {

    private final AdminAccountRepository adminAccountRepository;

    public Optional<AdminAccountDto> findUser(String username) {
        return Optional.empty();
    }

    public AdminAccountDto saveUser(String userId, String userPassword, Set<RoleType> roleTypes, String email, String nickname, String memo) {
        return null;
    }

    public List<AdminAccountDto> users() {
        return List.of();
    }

    public void deleteUser(String username) {

    }

}
