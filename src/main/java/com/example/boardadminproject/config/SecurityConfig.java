package com.example.boardadminproject.config;

import com.example.boardadminproject.domain.constant.RoleType;
import com.example.boardadminproject.dto.security.BoardAdminPrincipal;
import com.example.boardadminproject.dto.security.KakaoOAuth2Response;
import com.example.boardadminproject.service.AdminAccountService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Set;
import java.util.UUID;

/**
 * @author daecheol song
 * @since 1.0
 */
@Configuration
public class SecurityConfig {

    private static final String[] ROLES_ABOVE_MANAGER = {RoleType.MANAGER.name(), RoleType.DEVELOPER.name(), RoleType.ADMIN.name()};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.
                authorizeRequests(authRequest -> {
                    authRequest.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll();
                    authRequest.requestMatchers(new AntPathRequestMatcher("/**", HttpMethod.POST.name())).hasAnyRole(ROLES_ABOVE_MANAGER);
                    authRequest.requestMatchers(new AntPathRequestMatcher("/**", HttpMethod.DELETE.name())).hasAnyRole(ROLES_ABOVE_MANAGER);
                    authRequest.anyRequest().authenticated();
                })
                .formLogin(Customizer.withDefaults())
                .oauth2Login(Customizer.withDefaults())
                .logout(logout -> logout.logoutSuccessUrl("/"))
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(AdminAccountService adminAccountService) {
        return username -> adminAccountService
                .findUser(username)
                .map(BoardAdminPrincipal::from)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다. username: " + username));
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService(AdminAccountService adminAccountService, PasswordEncoder passwordEncoder) {
        final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        return userRequest -> {
            OAuth2User oAuth2User = delegate.loadUser(userRequest);

            KakaoOAuth2Response kakaoResponse = KakaoOAuth2Response.from(oAuth2User.getAttributes());
            String registrationId = userRequest.getClientRegistration().getRegistrationId();
            String providerId = String.valueOf(kakaoResponse.id());
            String username = registrationId + "_" + providerId;
            String dummyPassword = passwordEncoder.encode("{bcrypt}" + UUID.randomUUID());
            Set<RoleType> roleTypes = Set.of(RoleType.USER);

            return adminAccountService.findUser(username)
                    .map(BoardAdminPrincipal::from)
                    .orElseGet(() ->
                            BoardAdminPrincipal.from(
                                    adminAccountService.saveUser(
                                            username,
                                            dummyPassword,
                                            roleTypes,
                                            kakaoResponse.email(),
                                            kakaoResponse.nickname(),
                                            null
                                    )
                            )
                    );
        };
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
