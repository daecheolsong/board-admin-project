package com.example.boardadminproject.repository;

import com.example.boardadminproject.domain.UserAccount;
import com.example.boardadminproject.domain.constant.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;
import java.util.Optional;
import java.util.Set;


import static org.assertj.core.api.Assertions.*;

/**
 * @author daecheol song
 * @since 1.0
 */
@DisplayName("JPA 연결 테스트")
@DataJpaTest
@Import(JpaRepositoryTest.TestJpaConfig.class)
public class JpaRepositoryTest {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @DisplayName("회원 정보 select 테스트")
    @Test
    public void givenUserAccounts_whenSelecting_thenWorksFine() {

        List<UserAccount> userAccounts = userAccountRepository.findAll();

        assertThat(userAccounts)
                .isNotNull()
                .hasSize(4);
    }

    @DisplayName("회원 정보 insert 테스트")
    @Test
    public void givenUserAccount_whenSaving_thenWorksFine() {

        long previousCount = userAccountRepository.count();
        UserAccount userAccount = UserAccount.of("test", "test", Set.of(RoleType.USER), "test@email.com", "Test", null);

        userAccountRepository.save(userAccount);

        assertThat(userAccountRepository.count()).isEqualTo(previousCount + 1);
    }

    @DisplayName("회원 정보 update 테스트")
    @Test
    public void givenUserAccountAndRoleType_whenUpdating_thenWorksFine() {

        UserAccount userAccount = userAccountRepository.findByUserId("song");
        userAccount.addRoleType(RoleType.DEVELOPER);
        userAccount.addRoleTypes(List.of(RoleType.USER, RoleType.USER));
        userAccount.removeRoleType(RoleType.ADMIN);

        UserAccount updatedUserAccount = userAccountRepository.saveAndFlush(userAccount);

        assertThat(userAccount)
                .hasFieldOrPropertyWithValue("userId", "song")
                .hasFieldOrPropertyWithValue("roleTypes", Set.of(RoleType.USER, RoleType.DEVELOPER));
    }

    @DisplayName("회원 정보 delete 테스트")
    @Test
    public void givenUserAccount_whenDeleting_thenWorksFine() {

        long previousCount = userAccountRepository.count();
        UserAccount userAccount = userAccountRepository.findByUserId("song");

        userAccountRepository.delete(userAccount);

        assertThat(userAccountRepository.count())
                .isEqualTo(previousCount - 1);
        assertThat(userAccountRepository.findByUserId("song")).isNull();
    }


    @TestConfiguration
    @EnableJpaAuditing
    static class TestJpaConfig {

        @Bean
        public AuditorAware<String> auditorAware() {
            return () -> Optional.of("song");
        }
    }
}
