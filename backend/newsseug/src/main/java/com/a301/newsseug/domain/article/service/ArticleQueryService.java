package com.a301.newsseug.domain.article.service;

import com.a301.newsseug.domain.article.factory.ArticleConditionFactory;
import com.a301.newsseug.domain.article.model.dto.ArticleSummaryDto;
import com.a301.newsseug.domain.article.model.dto.response.GetArticleSummaryResponseDto;
import com.a301.newsseug.domain.article.model.entity.type.CategoryType;
import com.a301.newsseug.domain.article.repository.ArticleRepository;
import com.a301.newsseug.domain.press.model.entity.Press;
import com.a301.newsseug.global.util.ClockUtil;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleQueryService {

    private final int PAGE_SIZE = 10;
    private final ArticleRepository articleRepository;

    public Slice<ArticleSummaryDto> getSlicedArticlesByCategory(CategoryType category, int pageNumber) {
        return articleRepository.findAllArticlesByCondition(
                ArticleConditionFactory.create(category),
                PageRequest.of(pageNumber, PAGE_SIZE)
        );
    }

    public Slice<ArticleSummaryDto> getSlicedTodayArticlesByCategory(CategoryType category, int pageNumber) {
        LocalDateTime startOfDay = ClockUtil.getLocalDateTime().toLocalDate().atStartOfDay();
        return articleRepository.findAllArticlesByCondition(
                ArticleConditionFactory.create(category, startOfDay, startOfDay.plusDays(1)),
                PageRequest.of(pageNumber, PAGE_SIZE)
        );
    }

    public Slice<ArticleSummaryDto> getSlicedArticlesByPressAndCategory(Press press, CategoryType category, int pageNumber) {
        return articleRepository.findAllArticlesByPressAndCondition(
                ArticleConditionFactory.create(press, category),
                PageRequest.of(pageNumber, PAGE_SIZE)
        );
    }

    public Slice<ArticleSummaryDto> getSlicedArticlesBySubscribedPress(List<Press> presses, CategoryType category, int pageNumber) {
        return articleRepository.findAllArticlesBySubscribedPress(
                presses,
                ArticleConditionFactory.create(category),
                PageRequest.of(pageNumber, PAGE_SIZE)
        );
    }

    public Slice<ArticleSummaryDto> getSlicedArticlesByTitle(String title, int pageNumber) {
        return articleRepository.findAllArticlesByTitle(title, PageRequest.of(pageNumber, PAGE_SIZE));
    }

}
