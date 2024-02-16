package com.example.boardadminproject.controller;

import com.example.boardadminproject.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 회원 관리")
@Import(SecurityConfig.class)
@WebMvcTest
class UserAccountManagementControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("[view][GET] 회원 관리 페이지 - 정상 호출")
    public void given_whenRequestingUserAccountManagementView_thenReturnsUserAccountManagementView() throws Exception {

        mvc.perform(get("/management/user-accounts"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("management/user-accounts"));

    }
}