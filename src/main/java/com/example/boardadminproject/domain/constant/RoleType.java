package com.example.boardadminproject.domain.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author daecheol song
 * @since 1.0
 */
@RequiredArgsConstructor
public enum RoleType {

    USER("ROLE_USER"),
    MANAGER("ROLE_MANAGER"),
    DEVELOPER("ROLE_DEVELOPER"),
    ADMIN("ROLE_ADMIN");

    @Getter
    private final String roleName;

}