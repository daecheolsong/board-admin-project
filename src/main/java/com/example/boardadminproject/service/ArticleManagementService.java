package com.example.boardadminproject.service;

import com.example.boardadminproject.dto.ArticleDto;
import com.example.boardadminproject.dto.properties.ProjectProperties;
import com.example.boardadminproject.dto.response.ArticleClientResponse;
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
public class ArticleManagementService {

    private final RestTemplate restTemplate;
    private final ProjectProperties properties;

    public List<ArticleDto> getArticles() {

        URI uri = UriComponentsBuilder.fromHttpUrl(properties.board().url() + "/api/articles")
                .queryParam("size", 10000)
                .build()
                .toUri();

        ArticleClientResponse response = restTemplate.getForObject(uri, ArticleClientResponse.class);

        return Optional.ofNullable(response)
                .orElseGet(ArticleClientResponse::empty)
                .articles();
    }

    public ArticleDto getArticle(Long articleId) {

        URI uri = UriComponentsBuilder.fromHttpUrl(properties.board().url() + "/api/articles/" + articleId)
                .build()
                .toUri();

        ArticleDto response = restTemplate.getForObject(uri, ArticleDto.class);

        return Optional.ofNullable(response)
                .orElseThrow(() -> new NoSuchElementException("게시글이 없습니다 - articleId: " + articleId));
    }

    public void deleteArticle(Long articleId) {

        URI uri = UriComponentsBuilder.fromHttpUrl(properties.board().url() + "/api/articles/" + articleId)
                .build()
                .toUri();

        restTemplate.delete(uri);
    }
}
