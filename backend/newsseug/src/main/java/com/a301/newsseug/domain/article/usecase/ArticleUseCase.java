package com.a301.newsseug.domain.article.usecase;

import com.a301.newsseug.domain.article.model.dto.response.GetArticleSummaryResponseDto;
import com.a301.newsseug.domain.article.model.dto.response.GetArticleDetailResponseDto;
import com.a301.newsseug.domain.article.model.entity.Article;
import com.a301.newsseug.domain.article.model.entity.type.CategoryType;
import com.a301.newsseug.domain.article.service.ArticleCacheManager;
import com.a301.newsseug.domain.article.service.ArticleCountingService;
import com.a301.newsseug.domain.article.service.ArticleQueryService;
import com.a301.newsseug.domain.article.service.ArticleRankingService;
import com.a301.newsseug.domain.auth.model.entity.CustomUserDetails;
import com.a301.newsseug.domain.reaction.model.dto.ReactionDetailDto;
import com.a301.newsseug.domain.reaction.model.entity.type.ReactionType;
import com.a301.newsseug.domain.interaction.service.HistoryService;
import com.a301.newsseug.domain.reaction.service.ReactionService;
import com.a301.newsseug.domain.member.model.entity.Member;
import com.a301.newsseug.domain.member.model.entity.Subscribe;
import com.a301.newsseug.domain.member.service.SubscribeService;
import com.a301.newsseug.domain.press.model.entity.Press;
import com.a301.newsseug.domain.press.service.PressCacheManager;
import com.a301.newsseug.global.model.dto.SlicedResponse;
import com.a301.newsseug.global.model.entity.SliceDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleUseCase {

    private final ArticleCacheManager articleCacheManager;
    private final ArticleQueryService articleRetrieveService;
    private final ArticleCountingService articleCountingService;
    private final ArticleRankingService articleRankingService;
    private final PressCacheManager pressCacheManager;
    private final ReactionService reactionService;
    private final SubscribeService subscribeService;
    private final HistoryService historyService;
    private final ArticleQueryService articleQueryService;

    /**
     * [단일 기사 조회]
     * articleId에 대한 기사를 상세 조회
     *
     * @param userDetails   유저 정보
     * @param articleId     조회할 기사
     */
    public GetArticleDetailResponseDto retrieveArticleDetails(
            CustomUserDetails userDetails, Long articleId
    ) {

        Article article = articleCacheManager.getCachedArticle(articleId);

        Member loginedMember = null;
        if (userDetails.isEnabled()) {
            loginedMember = userDetails.getMember();
            historyService.createHistory(loginedMember, article);
        }

        articleRankingService.increaseRanking(articleId, 1);
        Long viewCount = articleCountingService.increaseViewCount(articleId);
        Long likeCount = articleCountingService.getLikeCount(articleId);
        Long hateCount = articleCountingService.getHateCount(articleId);

        if (!userDetails.isEnabled()) {
            return GetArticleDetailResponseDto.of(
                    article,
                    article.getViewCount() + viewCount,
                    false,
                    ReactionDetailDto.of(ReactionType.LIKE, false, article.getLikeCount() + likeCount),
                    ReactionDetailDto.of(ReactionType.HATE, false, article.getHateCount() + hateCount)
            );
        }

        return GetArticleDetailResponseDto.of(
                article,
                article.getViewCount() + viewCount,
                subscribeService.isSubscribed(loginedMember, article.getPress()),
                ReactionDetailDto.of(
                        ReactionType.LIKE,
                        reactionService.hasReacted(loginedMember, article, ReactionType.LIKE),
                        article.getLikeCount() + likeCount
                ),
                ReactionDetailDto.of(
                        ReactionType.HATE,
                        reactionService.hasReacted(loginedMember, article, ReactionType.HATE),
                        article.getHateCount() + hateCount
                )
        );

    }

    /**
     * [카테고리별 기사 조회]
     * 필터를 선택한 경우에는 해당 카테고리 기사만, 아닌 경우엔 구분 없이 모든 기사를 조회
     *
     * @param category       카테고리 (없으면 전체)
     * @param pageNumber    페이지 번호
     */
    public SlicedResponse<List<GetArticleSummaryResponseDto>> retrieveArticlesByCategory(String category, int pageNumber) {
        Slice<GetArticleSummaryResponseDto> articleSummaries = articleRetrieveService.getSlicedArticlesByCategory(CategoryType.from(category), pageNumber);
        return SlicedResponse.of(
                SliceDetails.of(articleSummaries.getNumber(), articleSummaries.isFirst(), articleSummaries.hasNext()),
                articleSummaries.getContent()
        );
    }

    /**
     * [조회수 기준 상위 10개 기사 조회]
     * 1시간 동안 집계된 조회수를 기준으로 상위 N개의 기사를 조회
     *
     * @param topN     상위 N개에 대한 정보
     */
    public List<GetArticleSummaryResponseDto> retrieveTopNArticles(long topN) {
        return articleRankingService.getRealTimeTopNArticles(topN);
    }

    /**
     * [오늘의 뉴스 조회]
     * 오늘 올라온 뉴스만 조회
     *
     * @param category      카테고리 (없으면 전체)
     * @param pageNumber    페이지 번호
     */
    public SlicedResponse<List<GetArticleSummaryResponseDto>> retrieveTodayArticles(String category, int pageNumber) {
        Slice<GetArticleSummaryResponseDto> articleSummaries = articleRetrieveService.getSlicedTodayArticlesByCategory(CategoryType.from(category), pageNumber);
        return SlicedResponse.of(
                SliceDetails.of(articleSummaries.getNumber(), articleSummaries.isFirst(), articleSummaries.hasNext()),
                articleSummaries.getContent()
        );
    }

    /**
     * [언론사별 기사 조회]
     * 언론사별 기사 목록 조회
     *
     * @param pressId
     * @param category      카테고리 (없으면 전체)
     * @param pageNumber    페이지 번호
     */
    public SlicedResponse<List<GetArticleSummaryResponseDto>> retrieveArticlesByPress(
            Long pressId, String category, int pageNumber
    ) {
        Press press = pressCacheManager.getCachedPress(pressId);
        Slice<GetArticleSummaryResponseDto> articleSummaries =
                articleRetrieveService.getSlicedArticlesByPressAndCategory(press, CategoryType.from(category), pageNumber);
        return SlicedResponse.of(
                SliceDetails.of(articleSummaries.getNumber(), articleSummaries.isFirst(), articleSummaries.hasNext()),
                articleSummaries.getContent()
        );
    }

    /**
     * [구독한 언론사 기사 조회]
     * 구독한 언론사에 대한 기사 목록 조회
     *
     * @param userDetails
     * @param category      카테고리 (없으면 전체)
     * @param pageNumber    페이지 번호
     */
    public SlicedResponse<List<GetArticleSummaryResponseDto>> retrieveArticlesBySubscribedPress(
            CustomUserDetails userDetails, String category, int pageNumber
    ) {
        List<Subscribe> subscribes = subscribeService.getSubscribeByMember(userDetails.getMember());
        Slice<GetArticleSummaryResponseDto> articleSummaries =
                articleQueryService.getSlicedArticlesBySubscribedPress(
                        subscribes.stream().map(Subscribe::getPress).toList(),
                        CategoryType.from(category),
                        pageNumber
                );
        return SlicedResponse.of(
                SliceDetails.of(articleSummaries.getNumber(), articleSummaries.isFirst(), articleSummaries.hasNext()),
                articleSummaries.getContent()
        );
    }

}
