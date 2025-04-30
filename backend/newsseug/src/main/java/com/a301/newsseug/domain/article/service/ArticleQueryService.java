package com.a301.newsseug.domain.article.service;

import com.a301.newsseug.domain.article.model.dto.ArticleRetrieveConditionDto;
import com.a301.newsseug.domain.article.model.entity.Article;
import com.a301.newsseug.domain.article.model.entity.type.CategoryType;
import com.a301.newsseug.domain.article.model.entity.type.ConversionStatus;
import com.a301.newsseug.domain.article.repository.ArticleRepository;
import com.a301.newsseug.global.model.entity.ActivationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleQueryService {

    private final ArticleRepository articleRepository;

    public Article getArticle(Long articleId) {
        return articleRepository.findOrThrow(articleId);
    }

    public Slice<Article> getSlicedArticles(int pageNumber) {
        return articleRepository.findAll(
                ArticleRetrieveConditionDto.of(ActivationStatus.ACTIVE, ConversionStatus.SUCCESS),
                PageRequest.of(pageNumber, 10)
        );
    }

    public Slice<Article> getSlicedArticlesByCategory(CategoryType category, int pageNumber) {

        return articleRepository.findAllByCategory(
                ArticleRetrieveConditionDto.of(category, ActivationStatus.ACTIVE, ConversionStatus.SUCCESS),
                PageRequest.of(pageNumber, 10)
        );

    }

}
