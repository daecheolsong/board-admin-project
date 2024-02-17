package com.example.boardadminproject.dto.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author daecheol song
 * @since 1.0
 */
@ConfigurationProperties("project")
public record ProjectProperties(Board board) {

    public record Board(String url) {
    }

}
