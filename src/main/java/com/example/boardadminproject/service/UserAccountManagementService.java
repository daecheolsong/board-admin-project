package com.example.boardadminproject.service;

import com.example.boardadminproject.dto.UserAccountDto;
import com.example.boardadminproject.dto.properties.ProjectProperties;
import com.example.boardadminproject.dto.response.UserAccountClientResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * @author daecheol song
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class UserAccountManagementService {

    private final RestTemplate restTemplate;
    private final ProjectProperties properties;

    public List<UserAccountDto> getUserAccounts() {

        URI uri = UriComponentsBuilder.fromHttpUrl(properties.board().url() + "/api/userAccounts")
                .queryParam("size", 10000)
                .build()
                .toUri();

        UserAccountClientResponse response = restTemplate.getForObject(uri, UserAccountClientResponse.class);

        return Optional.ofNullable(response).orElseGet(UserAccountClientResponse::empty).userAccounts();
    }

    public UserAccountDto getUserAccount(String userId) {

        URI uri = UriComponentsBuilder.fromHttpUrl(properties.board().url() + "/api/userAccounts?userId=" + userId)
                .build()
                .toUri();

        UserAccountClientResponse response = restTemplate.getForObject(uri, UserAccountClientResponse.class);


        return Optional.ofNullable(response)
                .orElseThrow(() -> new NoSuchElementException("해당 유저가 존재하지 않습니다. - userId: " + userId))
                .userAccounts()
                .stream().findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 유저가 존재하지 않습니다. - userId: " + userId));
    }

    public void deleteUserAccount(String userId) {

        URI uri = UriComponentsBuilder.fromHttpUrl(properties.board().url() + "/api/userAccounts/" + userId)
                .build()
                .toUri();

        restTemplate.delete(uri);
    }

}
