package com.example.boardadminproject.controller;

import com.example.boardadminproject.config.TestGlobalControllerConfig;
import com.example.boardadminproject.config.TestSecurityConfig;
import com.example.boardadminproject.service.AdminAccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 어드민 회원")
@Import({TestSecurityConfig.class, TestGlobalControllerConfig.class})
@WebMvcTest(AdminAccountController.class)
class AdminAccountControllerTest {

    @Autowired
    private MockMvc mvc;


    @WithMockUser(username = "tester", roles = "USER")
    @Test
    @DisplayName("[view][GET] 어드민 회원 페이지 - 정상 호출")
    public void given_whenRequestingAdminUserAccountView_thenReturnsAdminUserAccountView () throws Exception {

        mvc.perform(get("/admin/members"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("admin/members"));

    }

}