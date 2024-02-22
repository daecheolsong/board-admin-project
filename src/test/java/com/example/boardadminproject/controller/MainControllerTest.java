package com.example.boardadminproject.controller;

import com.example.boardadminproject.config.TestSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 루트")
@Import(TestSecurityConfig.class)
@WebMvcTest
class MainControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("[view][GET] 루트 페이지 -> 게시글 관리 페이지 Forwarding")
    public void given_whenRequestingRootView_thenForwardsToArticleManagementView() throws Exception {

        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/management/articles"))
                .andExpect(forwardedUrl("/management/articles"));

    }
}