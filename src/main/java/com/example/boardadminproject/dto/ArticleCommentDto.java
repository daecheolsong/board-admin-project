package com.example.boardadminproject.dto;

import java.time.LocalDateTime;

/**
 * @author daecheol song
 * @since 1.0
 */
public record ArticleCommentDto(
        Long id,
        Long articleId,
        UserAccountDto userAccount,
        Long parentCommentId,
        String content,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {

    public static ArticleCommentDto of(Long id, Long articleId, UserAccountDto userAccountDto, Long parentCommentId, String content, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new ArticleCommentDto(id, articleId, userAccountDto, parentCommentId, content, createdAt, createdBy, modifiedAt, modifiedBy);
    }

}