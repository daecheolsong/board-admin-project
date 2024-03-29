package com.example.boardadminproject.dto.response;

import com.example.boardadminproject.dto.UserAccountDto;

import java.time.LocalDateTime;

/**
 * @author daecheol song
 * @since 1.0
 */
public record UserAccountResponse(
        String userId,
        String email,
        String nickname,
        String memo,
        LocalDateTime createdAt,
        String createdBy
) {

    public static UserAccountResponse of(String userId, String email, String nickname, String memo, LocalDateTime createdAt, String createdBy) {
        return new UserAccountResponse(userId, email, nickname, memo, createdAt, createdBy);
    }

    public static UserAccountResponse from(UserAccountDto dto) {
        return UserAccountResponse.of(
                dto.userId(),
                dto.email(),
                dto.nickname(),
                dto.memo(),
                dto.createdAt(),
                dto.createdBy()
        );
    }
}
