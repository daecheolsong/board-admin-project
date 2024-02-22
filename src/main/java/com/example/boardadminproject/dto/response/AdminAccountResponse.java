package com.example.boardadminproject.dto.response;

import com.example.boardadminproject.domain.constant.RoleType;
import com.example.boardadminproject.dto.AdminAccountDto;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * @author daecheol song
 * @since 1.0
 */
public record AdminAccountResponse(
        String userId,
        String roleTypes,
        String email,
        String nickname,
        String momo,
        LocalDateTime createdAt,
        String createdBy) {

    public static AdminAccountResponse of(String userId, String roleTypes, String email, String nickname, String memo, LocalDateTime createdAt, String createdBy) {
        return new AdminAccountResponse(userId, roleTypes, email, nickname, memo, createdAt, createdBy);
    }

    public static AdminAccountResponse from(AdminAccountDto dto) {
        return AdminAccountResponse.of(
                dto.userId(),
                dto.roleTypes().stream()
                        .map(RoleType::getDescription)
                        .collect(Collectors.joining(", ")),
                dto.email(),
                dto.nickname(),
                dto.memo(),
                dto.createdAt(),
                dto.createdBy()
        );
    }

}
