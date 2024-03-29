package com.example.boardadminproject.controller;

import com.example.boardadminproject.config.TestGlobalControllerConfig;
import com.example.boardadminproject.config.TestSecurityConfig;
import com.example.boardadminproject.dto.ArticleCommentDto;
import com.example.boardadminproject.dto.UserAccountDto;
import com.example.boardadminproject.service.ArticleCommentManagementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("컨트롤러 - 댓글 관리")
@Import({TestSecurityConfig.class, TestGlobalControllerConfig.class})
@WebMvcTest(ArticleCommentManagementController.class)
class ArticleCommentManagementControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ArticleCommentManagementService articleCommentManagementService;

    @WithMockUser(username = "tester", roles = "USER")
    @Test
    @DisplayName("[view][GET] 댓글 관리 페이지 - 정상 호출")
    public void given_whenRequestingArticleCommentManagementView_thenReturnsArticleCommentManagementView() throws Exception {

        given(articleCommentManagementService.getArticleComments()).willReturn(List.of());

        mvc.perform(get("/management/article-comments"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("management/article-comments"))
                .andExpect(model().attribute("comments", List.of()));
        then(articleCommentManagementService).should().getArticleComments();

    }


    @WithMockUser(username = "tester", roles = "USER")
    @DisplayName("[data][GET] 댓글 1개 - 정상 호출")
    @Test
    void givenCommentId_whenRequestingArticleComment_thenReturnsArticleComment() throws Exception {

        Long articleCommentId = 1L;
        ArticleCommentDto articleCommentDto = createArticleCommentDto("comment");
        given(articleCommentManagementService.getArticleComment(articleCommentId)).willReturn(articleCommentDto);

        mvc.perform(get("/management/article-comments/" + articleCommentId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(articleCommentId))
                .andExpect(jsonPath("$.content").value(articleCommentDto.content()))
                .andExpect(jsonPath("$.userAccount.nickname").value(articleCommentDto.userAccount().nickname()));
        then(articleCommentManagementService).should().getArticleComment(articleCommentId);
    }

    @WithMockUser(username = "tester", roles = "MANAGER")
    @DisplayName("[view][POST] 댓글 삭제 - 정상 호출")
    @Test
    void givenCommentId_whenRequestingDeletion_thenRedirectsToArticleCommentManagementView() throws Exception {

        Long articleCommentId = 1L;
        willDoNothing().given(articleCommentManagementService).deleteArticleComment(articleCommentId);

        mvc.perform(post("/management/article-comments/" + articleCommentId)
                                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/management/article-comments"))
                .andExpect(redirectedUrl("/management/article-comments"));
        then(articleCommentManagementService).should().deleteArticleComment(articleCommentId);
    }


    private ArticleCommentDto createArticleCommentDto(String content) {
        return ArticleCommentDto.of(
                1L,
                1L,
                createUserAccountDto(),
                null,
                content,
                LocalDateTime.now(),
                "Song",
                LocalDateTime.now(),
                "Song"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                "tester",
                "tester@email.com",
                "tester",
                "test memo"
        );
    }

}