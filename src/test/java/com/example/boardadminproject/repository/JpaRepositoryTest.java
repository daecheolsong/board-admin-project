package com.example.boardadminproject.repository;

import com.example.boardadminproject.domain.AdminAccount;
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
    private AdminAccountRepository adminAccountRepository;

    @DisplayName("회원 정보 select 테스트")
    @Test
    public void givenAdminAccounts_whenSelecting_thenWorksFine() {

        List<AdminAccount> userAccounts = adminAccountRepository.findAll();

        assertThat(userAccounts)
                .isNotNull()
                .hasSize(4);
    }

    @DisplayName("회원 정보 insert 테스트")
    @Test
    public void givenAdminAccount_whenSaving_thenWorksFine() {

        long previousCount = adminAccountRepository.count();
        AdminAccount userAccount = AdminAccount.of("test", "test", Set.of(RoleType.USER), "test@email.com", "Test", null);

        adminAccountRepository.save(userAccount);

        assertThat(adminAccountRepository.count()).isEqualTo(previousCount + 1);
    }

    @DisplayName("회원 정보 update 테스트")
    @Test
    public void givenAdminAccountAndRoleType_whenUpdating_thenWorksFine() {

        AdminAccount userAccount = adminAccountRepository.findByUserId("song");
        userAccount.addRoleType(RoleType.DEVELOPER);
        userAccount.addRoleTypes(List.of(RoleType.USER, RoleType.USER));
        userAccount.removeRoleType(RoleType.ADMIN);

        AdminAccount updatedUserAccount = adminAccountRepository.saveAndFlush(userAccount);

        assertThat(userAccount)
                .hasFieldOrPropertyWithValue("userId", "song")
                .hasFieldOrPropertyWithValue("roleTypes", Set.of(RoleType.USER, RoleType.DEVELOPER));
    }

    @DisplayName("회원 정보 delete 테스트")
    @Test
    public void givenAdminAccount_whenDeleting_thenWorksFine() {

        long previousCount = adminAccountRepository.count();
        AdminAccount userAccount = adminAccountRepository.findByUserId("song");

        adminAccountRepository.delete(userAccount);

        assertThat(adminAccountRepository.count())
                .isEqualTo(previousCount - 1);
        assertThat(adminAccountRepository.findByUserId("song")).isNull();
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
