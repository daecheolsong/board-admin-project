package com.example.boardadminproject.service;

import com.example.boardadminproject.dto.ArticleCommentDto;
import com.example.boardadminproject.dto.properties.ProjectProperties;
import com.example.boardadminproject.dto.response.ArticleCommentClientResponse;
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
public class ArticleCommentManagementService {

    private final RestTemplate restTemplate;
    private final ProjectProperties properties;

    public List<ArticleCommentDto> getArticleComments() {

        URI uri = UriComponentsBuilder.fromHttpUrl(properties.board().url() + "/api/articleComments")
                .queryParam("size", 10000)
                .build()
                .toUri();

        ArticleCommentClientResponse response = restTemplate.getForObject(uri, ArticleCommentClientResponse.class);

        return Optional.ofNullable(response).orElseGet(ArticleCommentClientResponse::empty).articleComments();
    }

    public ArticleCommentDto getArticleComment(Long articleCommentId) {

        URI uri = UriComponentsBuilder.fromHttpUrl(properties.board().url() + "/api/articleComments/" + articleCommentId)
                .build()
                .toUri();

        ArticleCommentDto response = restTemplate.getForObject(uri, ArticleCommentDto.class);

        return Optional.ofNullable(response).orElseThrow(() -> new NoSuchElementException("댓글이 없습니다 - articleCommentId : " + articleCommentId));
    }

    public void deleteArticleComment(Long articleCommentId) {
        URI uri = UriComponentsBuilder.fromHttpUrl(properties.board().url() + "/api/articleComments/" + articleCommentId)
                .build()
                .toUri();

        restTemplate.delete(uri);
    }

}