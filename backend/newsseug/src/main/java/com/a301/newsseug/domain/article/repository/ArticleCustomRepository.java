package com.a301.newsseug.domain.article.repository;

import com.a301.newsseug.domain.article.model.dto.ArticleRetrieveCondition;
import com.a301.newsseug.domain.article.model.dto.response.GetArticleSummaryResponseDto;
import com.a301.newsseug.domain.article.model.entity.Article;
import com.a301.newsseug.domain.press.model.entity.Press;
import java.util.Map;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ArticleCustomRepository {

    Slice<GetArticleSummaryResponseDto> findAllArticlesByCondition(ArticleRetrieveCondition conditions, Pageable pageable);

    Slice<GetArticleSummaryResponseDto> findAllArticlesByPressAndCondition(ArticleRetrieveCondition conditions, Pageable pageable);

    Slice<GetArticleSummaryResponseDto> findAllArticlesBySubscribedPress(List<Press> pressList, ArticleRetrieveCondition conditions, Pageable pageable);

    Slice<GetArticleSummaryResponseDto> findAllArticlesByTitle(String keyword, Pageable pageable);

    void batchUpdateCount(String field, Map<String, Long> countingLog);

}
