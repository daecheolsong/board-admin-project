package com.example.boardadminproject.controller;

import com.example.boardadminproject.dto.websocket.WebSocketMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.security.Principal;

/**
 * @author daecheol song
 * @since 1.0
 */
@Controller
public class WebSocketController {

    @MessageMapping("/hello")
    @SendTo("/topic/chat")
    public WebSocketMessage chat(WebSocketMessage message, Principal principal) {
        try {
            Thread.sleep(500); // 대화하는 느낌을 시뮬레이션
            return WebSocketMessage.of(principal.getName() + "님! \"" + message.content() + "\" 라고 하셨나요?");
        } catch (InterruptedException e) {
            return WebSocketMessage.of("서버 상태가 불안정합니다.");
        }
    }
}
