package com.example.boardadminproject.service;

import com.example.boardadminproject.domain.constant.RoleType;
import com.example.boardadminproject.dto.ArticleDto;
import com.example.boardadminproject.dto.UserAccountDto;
import com.example.boardadminproject.dto.properties.ProjectProperties;
import com.example.boardadminproject.dto.response.ArticleClientResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

/**
 * @author daecheol song
 * @since 1.0
 */
@ActiveProfiles("test")
@DisplayName("비즈니스 로직 - 게시글 관리")
public class ArticleManagementServiceTest {


    @Disabled("실제 API 호출 결과 관찰용이므로 평상시엔 비활성화")
    @DisplayName("실제 API 호출 테스트")
    @SpringBootTest
    @Nested
    class RealApiTest {

        @Autowired
        private ArticleManagementService sut;

        @DisplayName("게시글 API 를 호출하면, 게시글을 가져온다.")
        @Test
        public void givenNothing_whenRequestingArticlesApi_thenReturnArticles() {

            List<ArticleDto> result = sut.getArticles();

            assertThat(result).isNotNull();
        }
    }


    @DisplayName("API Mocking 테스트")
    @EnableConfigurationProperties(ProjectProperties.class)
    @AutoConfigureWebClient(registerRestTemplate = true)
    @RestClientTest(ArticleManagementService.class)
    @Nested
    class RestTemplateTest {

        @Autowired
        private ArticleManagementService sut;

        @Autowired
        private ProjectProperties properties;
        @Autowired
        private MockRestServiceServer server;
        @Autowired
        private ObjectMapper mapper;

        @DisplayName("게시글 목록 API를 호출하면, 게시글들을 가져온다.")
        @Test
        public void givenNothing_whenCallingArticlesApi_thenReturnArticles() throws Exception {

            ArticleDto article = createArticleDto("제목", "글");
            ArticleClientResponse articleClientResponse = ArticleClientResponse.of(List.of(article));
            server.expect(requestTo(properties.board().url() + "/api/articles?size=10000"))
                    .andRespond(withSuccess(
                            mapper.writeValueAsBytes(articleClientResponse),
                            MediaType.APPLICATION_JSON
                    ));

            List<ArticleDto> result = sut.getArticles();

            assertThat(result).first()
                    .hasFieldOrPropertyWithValue("id", article.id())
                    .hasFieldOrPropertyWithValue("title", article.title())
                    .hasFieldOrPropertyWithValue("content", article.content())
                    .hasFieldOrPropertyWithValue("userAccount.nickname", article.userAccount().nickname());
            server.verify();
        }

        @DisplayName("게시글 ID와 함께 게시글 API을 호출하면, 게시글을 가져온다.")
        @Test
        void givenArticleId_whenCallingArticleApi_thenReturnsArticle() throws Exception {

            Long articleId = 1L;
            ArticleDto expectedArticle = createArticleDto("게시판", "글");
            server
                    .expect(requestTo(properties.board().url() + "/api/articles/" + articleId))
                    .andRespond(withSuccess(
                            mapper.writeValueAsString(expectedArticle),
                            MediaType.APPLICATION_JSON
                    ));

            ArticleDto result = sut.getArticle(articleId);

            assertThat(result)
                    .hasFieldOrPropertyWithValue("id", expectedArticle.id())
                    .hasFieldOrPropertyWithValue("title", expectedArticle.title())
                    .hasFieldOrPropertyWithValue("content", expectedArticle.content())
                    .hasFieldOrPropertyWithValue("userAccount.nickname", expectedArticle.userAccount().nickname());
            server.verify();
        }

        @DisplayName("게시글 ID와 함께 게시글 삭제 API을 호출하면, 게시글을 삭제한다.")
        @Test
        void givenArticleId_whenCallingDeleteArticleApi_thenDeletesArticle() throws Exception {

            Long articleId = 1L;
            server.expect(requestTo(properties.board().url() + "/api/articles/" + articleId))
                    .andExpect(method(HttpMethod.DELETE))
                    .andRespond(withSuccess());

            sut.deleteArticle(articleId);

            server.verify();
        }

        private ArticleDto createArticleDto(String title, String content) {
            return ArticleDto.of(
                    1L,
                    createUserAccountDto(),
                    title,
                    content,
                    null,
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
}
