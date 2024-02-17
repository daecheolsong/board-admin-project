package com.example.boardadminproject.service;

import com.example.boardadminproject.domain.constant.RoleType;
import com.example.boardadminproject.dto.ArticleCommentDto;
import com.example.boardadminproject.dto.UserAccountDto;
import com.example.boardadminproject.dto.properties.ProjectProperties;
import com.example.boardadminproject.dto.response.ArticleCommentClientResponse;
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
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * @author daecheol song
 * @since 1.0
 */
@ActiveProfiles("test")
@DisplayName("비즈니스 로직 - 댓글 관리")
public class ArticleCommentManagementServiceTest {

    @Disabled("실제 API 호출 결과 관찰용이므로 평상시엔 비활성화")
    @DisplayName("실제 API 호출 테스트")
    @SpringBootTest
    @Nested
    class RealApiTest {

        @Autowired
        private ArticleCommentManagementService sut;


        @DisplayName("댓글 API를 호출하면, 댓글을 가져온다.")
        @Test
        void givenNothing_whenCallingCommentApi_thenReturnsCommentList() {

            List<ArticleCommentDto> result = sut.getArticleComments();

            assertThat(result).isNotNull();
        }
    }

    @DisplayName("API mocking 테스트")
    @EnableConfigurationProperties(ProjectProperties.class)
    @AutoConfigureWebClient(registerRestTemplate = true)
    @RestClientTest(ArticleCommentManagementService.class)
    @Nested
    class RestTemplateTest {

        @Autowired
        private ArticleCommentManagementService sut;

        @Autowired
        private ProjectProperties properties;

        @Autowired
        private MockRestServiceServer server;

        @Autowired
        private ObjectMapper mapper;


        @DisplayName("댓글 목록 API을 호출하면, 댓글들을 가져온다.")
        @Test
        void givenNothing_whenCallingCommentsApi_thenReturnsComments() throws Exception {

            ArticleCommentDto expectedComment = createArticleCommentDto("댓글");
            ArticleCommentClientResponse expectedResponse = ArticleCommentClientResponse.of(List.of(expectedComment));
            server
                    .expect(requestTo(properties.board().url() + "/api/articleComments?size=10000"))
                    .andRespond(withSuccess(
                            mapper.writeValueAsString(expectedResponse),
                            MediaType.APPLICATION_JSON
                    ));


            List<ArticleCommentDto> result = sut.getArticleComments();

            assertThat(result).first()
                    .hasFieldOrPropertyWithValue("id", expectedComment.id())
                    .hasFieldOrPropertyWithValue("content", expectedComment.content())
                    .hasFieldOrPropertyWithValue("userAccount.nickname", expectedComment.userAccount().nickname());
            server.verify();
        }

        @DisplayName("댓글 ID와 함께 댓글 API을 호출하면, 댓글을 가져온다.")
        @Test
        void givenCommentId_whenCallingCommentApi_thenReturnsComment() throws Exception {
            Long articleCommentId = 1L;
            ArticleCommentDto expectedComment = createArticleCommentDto("댓글");
            server
                    .expect(requestTo(properties.board().url() + "/api/articleComments/" + articleCommentId))
                    .andRespond(withSuccess(
                            mapper.writeValueAsString(expectedComment),
                            MediaType.APPLICATION_JSON
                    ));


            ArticleCommentDto result = sut.getArticleComment(articleCommentId);

            assertThat(result)
                    .hasFieldOrPropertyWithValue("id", expectedComment.id())
                    .hasFieldOrPropertyWithValue("content", expectedComment.content())
                    .hasFieldOrPropertyWithValue("userAccount.nickname", expectedComment.userAccount().nickname());
            server.verify();
        }

        @DisplayName("댓글 ID와 함께 댓글 삭제 API을 호출하면, 댓글을 삭제한다.")
        @Test
        void givenCommentId_whenCallingDeleteCommentApi_thenDeletesComment() throws Exception {

            Long articleCommentId = 1L;
            server
                    .expect(requestTo(properties.board().url() + "/api/articleComments/" + articleCommentId))
                    .andExpect(method(HttpMethod.DELETE))
                    .andRespond(withSuccess());

            sut.deleteArticleComment(articleCommentId);

            server.verify();
        }
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
