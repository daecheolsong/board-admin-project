package com.example.boardadminproject.service;

import com.example.boardadminproject.dto.UserAccountDto;
import com.example.boardadminproject.dto.properties.ProjectProperties;
import com.example.boardadminproject.dto.response.UserAccountClientResponse;
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
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * @author daecheol song
 * @since 1.0
 */
class UserAccountManagementServiceTest {

    @Disabled("실제 API 호출 결과 관찰용이므로 평상시엔 비활성화")
    @DisplayName("실제 API 호출 테스트")
    @SpringBootTest
    @Nested
    class RealApiTest {

        @Autowired
        private UserAccountManagementService sut;

        @DisplayName("회원 API을 호출하면, 회원 정보를 가져온다.")
        @Test
        void givenNothing_whenCallingUserAccountApi_thenReturnsUserAccountList() {

            List<UserAccountDto> result = sut.getUserAccounts();

            assertThat(result).isNotNull();
        }
    }


    @DisplayName("API mocking 테스트")
    @EnableConfigurationProperties(ProjectProperties.class)
    @AutoConfigureWebClient(registerRestTemplate = true)
    @RestClientTest(UserAccountManagementService.class)
    @Nested
    class RestTemplateTest {

        @Autowired
        private UserAccountManagementService sut;

        @Autowired
        private ProjectProperties projectProperties;
        @Autowired
        private MockRestServiceServer server;
        @Autowired
        private ObjectMapper mapper;


        @DisplayName("회원 목록 API을 호출하면, 회원들을 가져온다.")
        @Test
        void givenNothing_whenCallingUserAccountsApi_thenReturnsUserAccountList() throws Exception {

            UserAccountDto expectedUserAccount = createUserAccountDto("song", "Song");
            UserAccountClientResponse expectedResponse = UserAccountClientResponse.of(List.of(expectedUserAccount));
            server.expect(requestTo(projectProperties.board().url() + "/api/userAccounts?size=10000"))
                    .andRespond(withSuccess(
                            mapper.writeValueAsString(expectedResponse),
                            MediaType.APPLICATION_JSON
                    ));

            List<UserAccountDto> result = sut.getUserAccounts();

            assertThat(result).first()
                    .hasFieldOrPropertyWithValue("userId", expectedUserAccount.userId())
                    .hasFieldOrPropertyWithValue("nickname", expectedUserAccount.nickname());
            server.verify();
        }

        @DisplayName("회원 ID와 함께 회원 API을 호출하면, 회원을 가져온다.")
        @Test
        void givenUserAccountId_whenCallingUserAccountApi_thenReturnsUserAccount() throws Exception {

            String userId = "song";
            UserAccountClientResponse expectedResponse = createUserClientResponseWithUserId(userId);
            server
                    .expect(requestTo(projectProperties.board().url() + "/api/userAccounts?userId=" + userId))
                    .andRespond(withSuccess(
                            mapper.writeValueAsString(expectedResponse),
                            MediaType.APPLICATION_JSON
                    ));

            UserAccountDto result = sut.getUserAccount(userId);

            assertThat(result)
                    .hasFieldOrPropertyWithValue("userId", expectedResponse.userAccounts().get(0).userId())
                    .hasFieldOrPropertyWithValue("nickname", expectedResponse.userAccounts().get(0).nickname());
            server.verify();
        }

        @DisplayName("회원 ID와 함께 회원 삭제 API을 호출하면, 회원을 삭제한다.")
        @Test
        void givenUserAccountId_whenCallingDeleteUserAccountApi_thenDeletesUserAccount() throws Exception {

            String userId = "song";
            server.expect(requestTo(projectProperties.board().url() + "/api/userAccounts/" + userId))
                    .andExpect(method(HttpMethod.DELETE))
                    .andRespond(withSuccess());

            sut.deleteUserAccount(userId);

            server.verify();
        }
    }

    private UserAccountDto createUserAccountDto(String userId, String nickname) {
        return UserAccountDto.of(
                userId,
                "tester@email.com",
                nickname,
                "test memo"
        );
    }

    private UserAccountClientResponse createUserClientResponseWithUserId(String userId) {
        return UserAccountClientResponse
                .of(List.of(createUserAccountDto(userId, userId)));
    }

}