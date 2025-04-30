package com.a301.newsseug.domain.article.service;

import com.a301.newsseug.domain.article.model.dto.ArticleRetrieveConditionDto;
import com.a301.newsseug.domain.article.model.dto.response.GetArticleResponse;
import com.a301.newsseug.domain.article.model.entity.Article;
import com.a301.newsseug.domain.article.model.entity.type.CategoryType;
import com.a301.newsseug.domain.article.model.entity.type.ConversionStatus;
import com.a301.newsseug.domain.article.repository.ArticleRepository;
import com.a301.newsseug.global.model.dto.SlicedResponse;
import com.a301.newsseug.global.model.entity.ActivationStatus;
import com.a301.newsseug.global.model.entity.SliceDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleQueryService {

    private final ArticleRepository articleRepository;

    public SlicedResponse<List<GetArticleResponse>> getArticles(int pageNumber) {

        Slice<Article> slicedArticles = articleRepository.findAll(
                ArticleRetrieveConditionDto.of(ActivationStatus.ACTIVE, ConversionStatus.SUCCESS),
                PageRequest.of(pageNumber, 10)
        );

        return SlicedResponse.of(
                SliceDetails.of(slicedArticles.getNumber(), slicedArticles.isFirst(),
                        slicedArticles.hasNext()),
                GetArticleResponse.of(slicedArticles.getContent())
        );
    }

    public SlicedResponse<List<GetArticleResponse>> getArticlesByCategory(CategoryType category, int pageNumber) {

        Slice<Article> slicedArticles = articleRepository.findAllByCategory(
                ArticleRetrieveConditionDto.of(category, ActivationStatus.ACTIVE, ConversionStatus.SUCCESS),
                PageRequest.of(pageNumber, 10)
        );

        return SlicedResponse.of(
                SliceDetails.of(slicedArticles.getNumber(), slicedArticles.isFirst(), slicedArticles.hasNext()),
                GetArticleResponse.of(slicedArticles.getContent())
        );

    }

}
