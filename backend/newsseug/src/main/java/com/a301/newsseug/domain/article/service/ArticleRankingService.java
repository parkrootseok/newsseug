package com.a301.newsseug.domain.article.service;

import com.a301.newsseug.domain.article.model.dto.response.GetArticleResponse;
import com.a301.newsseug.domain.article.repository.ArticleRankingRepository;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleRankingService {

    private final String KEY = "article_ranking";

    private final ArticleCacheManager cacheManager;
    private final ArticleRankingRepository rankingRepository;


    public void increaseRanking(Long articleId, double score) {
        rankingRepository.incrementScore(KEY, articleId.toString(), score);
    }

    public List<GetArticleResponse> getRealTimeTopNArticles(long topN) {
        return rankingRepository.getTopN(KEY, topN).stream()
                .map(TypedTuple::getValue)
                .filter(Objects::nonNull)
                .filter(value -> value.chars().allMatch(Character::isDigit))
                .map(Long::parseLong)
                .map(cacheManager::getCachedArticle)
                .map(GetArticleResponse::of)
                .collect(Collectors.toList());
    }

}
