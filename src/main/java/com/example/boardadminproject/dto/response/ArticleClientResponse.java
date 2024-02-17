package com.example.boardadminproject.dto.response;

import com.example.boardadminproject.dto.ArticleDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author daecheol song
 * @since 1.0
 */
public record ArticleClientResponse(
        @JsonProperty("_embedded") Embedded embedded,
        @JsonProperty("page") Page page
) {

    public static ArticleClientResponse of(List<ArticleDto> articles) {
        return new ArticleClientResponse(new Embedded(articles), new Page(articles.size(), articles.size(), 1, 0));
    }

    public record Embedded(List<ArticleDto> articles) {
    }

    public record Page(int size, int totalElements, int totalPages, int number) {
    }

    public List<ArticleDto> articles() {
        return embedded().articles();
    }

}
