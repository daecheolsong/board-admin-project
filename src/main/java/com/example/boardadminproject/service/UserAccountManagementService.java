package com.example.boardadminproject.service;

import com.example.boardadminproject.dto.UserAccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author daecheol song
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class UserAccountManagementService {

    public List<UserAccountDto> getUserAccounts() {
        return List.of();
    }

    public UserAccountDto getUserAccount(String userId) {
        return null;
    }

    public void deleteUserAccount(String userId) {

    }

}
