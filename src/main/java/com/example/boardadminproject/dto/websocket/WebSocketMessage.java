package com.example.boardadminproject.dto.websocket;

/**
 * @author daecheol song
 * @since 1.0
 */
public record WebSocketMessage(String content) {
    public static WebSocketMessage of(String content) {
        return new WebSocketMessage(content);
    }
}
