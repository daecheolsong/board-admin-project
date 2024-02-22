package com.example.boardadminproject.service;

import com.example.boardadminproject.domain.AdminAccount;
import com.example.boardadminproject.domain.constant.RoleType;
import com.example.boardadminproject.dto.AdminAccountDto;
import com.example.boardadminproject.dto.UserAccountDto;
import com.example.boardadminproject.repository.AdminAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

/**
 * @author daecheol song
 * @since 1.0
 */
@DisplayName("비즈니스 로직 - 어드민 회원")
@ExtendWith(MockitoExtension.class)
public class AdminAccountServiceTest {

    @InjectMocks
    private AdminAccountService sut;

    @Mock
    private AdminAccountRepository adminAccountRepository;

    @DisplayName("존재하는 회원 ID를 검색하면, 회원 데이터를 optional로 반환한다.")
    @Test
    public void givenExistentUserId_whenSearching_thenReturnsOptionalUserData() {

        String username = "song";
        given(adminAccountRepository.findByUserId(username)).willReturn(createAdminAccount(username));

        Optional<AdminAccountDto> result = sut.findUser(username);

        assertThat(result).isPresent().map(AdminAccountDto::userId).hasValue(username);
        then(adminAccountRepository).should().findByUserId(username);
    }

    @DisplayName("존재하지 않는 회원 ID를 검색하면, 비어있는 Optional을 반환한다.")
    @Test
    public void givenNonExistentUserId_whenSearching_thenReturnsOptionalEmpty() {

        String username = "XXXXXX";
        given(adminAccountRepository.findByUserId(username)).willReturn(null);

        Optional<AdminAccountDto> result = sut.findUser(username);

        assertThat(result).isEmpty();
        then(adminAccountRepository).should().findByUserId(username);
    }

    @DisplayName("회원 정보를 입력하면, 새로운 회원 정보를 저장하여 가입시키고 해당 회원 데이터를 리턴한다.")
    @Test
    void givenUserParams_whenSaving_thenSavesAdminAccount() {

        AdminAccount adminAccount = createSigningUpAdminAccount("song", Set.of(RoleType.USER));
        given(adminAccountRepository.save(adminAccount)).willReturn(adminAccount);

        AdminAccountDto result = sut.saveUser(
                adminAccount.getUserId(),
                adminAccount.getUserPassword(),
                adminAccount.getRoleTypes(),
                adminAccount.getEmail(),
                adminAccount.getNickname(),
                adminAccount.getMemo()
        );

        assertThat(result)
                .hasFieldOrPropertyWithValue("userId", adminAccount.getUserId())
                .hasFieldOrPropertyWithValue("userPassword", adminAccount.getUserPassword())
                .hasFieldOrPropertyWithValue("roleTypes", adminAccount.getRoleTypes())
                .hasFieldOrPropertyWithValue("email", adminAccount.getEmail())
                .hasFieldOrPropertyWithValue("nickname", adminAccount.getNickname())
                .hasFieldOrPropertyWithValue("memo", adminAccount.getMemo())
                .hasFieldOrPropertyWithValue("createdBy", adminAccount.getUserId())
                .hasFieldOrPropertyWithValue("modifiedBy", adminAccount.getUserId());
        then(adminAccountRepository).should().save(adminAccount);
    }

    @DisplayName("전체 어드민 회원을 조회한다.")
    @Test
    void givenNothing_whenSelectingAdminAccounts_thenReturnsAllAdminAccounts() {

        given(adminAccountRepository.findAll()).willReturn(List.of());

        List<AdminAccountDto> result = sut.users();

        assertThat(result).hasSize(0);
        then(adminAccountRepository).should().findAll();
    }

    @DisplayName("회원 ID를 입력하면, 회원을 삭제한다.")
    @Test
    void givenUserId_whenDeleting_thenDeletesAdminAccount() {

        String userId = "song";
        willDoNothing().given(adminAccountRepository).deleteByUserId(userId);

        sut.deleteUser(userId);

        then(adminAccountRepository).should().deleteByUserId(userId);
    }


    private AdminAccount createAdminAccount(String username) {
        return createAdminAccount(username, Set.of(RoleType.USER), null);
    }

    private AdminAccount createSigningUpAdminAccount(String username, Set<RoleType> roleTypes) {
        return AdminAccount.of(
                username,
                "password",
                roleTypes,
                "e@mail.com",
                "nickname",
                "memo",
                username
        );
    }

    private AdminAccount createAdminAccount(String username, Set<RoleType> roleTypes, String createdBy) {
        return AdminAccount.of(
                username,
                "password",
                roleTypes,
                "e@mail.com",
                "nickname",
                "memo",
                createdBy
        );
    }
}
