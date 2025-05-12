package com.a301.newsseug.domain.article.usecase;

import com.a301.newsseug.domain.article.model.dto.response.GetArticleDetailsResponse;
import com.a301.newsseug.domain.article.model.dto.response.GetArticleResponse;
import com.a301.newsseug.domain.article.model.entity.Article;
import com.a301.newsseug.domain.article.model.entity.type.CategoryType;
import com.a301.newsseug.domain.article.service.ArticleCountingService;
import com.a301.newsseug.domain.article.service.ArticleQueryService;
import com.a301.newsseug.domain.article.service.ArticleRankingService;
import com.a301.newsseug.domain.article.service.BirthYearCountService;
import com.a301.newsseug.domain.auth.model.entity.CustomUserDetails;
import com.a301.newsseug.domain.interaction.model.dto.SimpleHateDto;
import com.a301.newsseug.domain.interaction.model.dto.SimpleLikeDto;
import com.a301.newsseug.domain.interaction.model.entity.type.ReactionType;
import com.a301.newsseug.domain.interaction.service.HistoryService;
import com.a301.newsseug.domain.interaction.service.ReactionService;
import com.a301.newsseug.domain.member.model.entity.Member;
import com.a301.newsseug.domain.member.service.SubscribeService;
import com.a301.newsseug.global.model.dto.SlicedResponse;
import com.a301.newsseug.global.model.entity.SliceDetails;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleUseCase {

    private final ArticleQueryService queryService;
    private final ArticleCountingService countingService;
    private final ArticleRankingService rankingService;
    private final ReactionService reactionService;
    private final SubscribeService subscribeService;
    private final HistoryService historyService;
    private final BirthYearCountService birthYearCountService;

    /**
     * [단일 기사 조회]
     * articleId에 대한 기사를 상세 조회
     *
     * @param userDetails   유저 정보
     * @param articleId     조회할 기사
     */
    public GetArticleDetailsResponse retrieveArticleDetails(
            CustomUserDetails userDetails, Long articleId
    ) {

        Article article = queryService.getArticle(articleId);

        Member loginedMember = null;
        if (Objects.nonNull(userDetails)) {
            loginedMember = userDetails.getMember();
            historyService.createHistory(loginedMember, article);
            birthYearCountService.incrementBirthYearCount(loginedMember, article);
        }

        rankingService.increaseRanking(articleId, 1);
        Long viewCount = countingService.increaseViewCount(articleId);
        Long likeCount = countingService.getLikeCount(articleId);
        Long hateCount = countingService.getHateCount(articleId);


        if (Objects.isNull(loginedMember)) {
            return GetArticleDetailsResponse.of(
                    article,
                    article.getViewCount() + viewCount,
                    false,
                    SimpleLikeDto.of(false, article.getLikeCount() + likeCount),
                    SimpleHateDto.of(false, article.getHateCount() + hateCount)
            );
        }

        return GetArticleDetailsResponse.of(
                article,
                article.getViewCount() + viewCount,
                subscribeService.isSubscribed(loginedMember, article.getPress()),
                SimpleLikeDto.of(
                        reactionService.hasReacted(userDetails.getMember(), article, ReactionType.LIKE),
                        article.getLikeCount() + likeCount
                ),
                SimpleHateDto.of(
                        reactionService.hasReacted(userDetails.getMember(), article, ReactionType.HATE),
                        article.getHateCount() + hateCount
                )
        );

    }

    /**
     * [전체 기사 조회]
     * 필터를 선택한 경우에는 해당 카테고리 기사만, 아닌 경우엔 구분 없이 모든 기사를 조회
     *
     * @param filter        카테고리 (없으면 전체)
     * @param pageNumber    페이지 번호
     */
    public SlicedResponse<List<GetArticleResponse>> retrieveAllArticle(String filter, int pageNumber) {
        CategoryType category = CategoryType.from(filter);

        Slice<Article> slicedArticles;
        if (category.equals(CategoryType.ALL)) {
            // 카테고리를 선택하지 않은 경우
            slicedArticles = queryService.getSlicedArticles(pageNumber);
        } else {
            // 특정 카테고리를 선택한 경우
            slicedArticles = queryService.getSlicedArticlesByCategory(category, pageNumber);
        }

        return SlicedResponse.of(
                SliceDetails.of(slicedArticles.getNumber(), slicedArticles.isFirst(), slicedArticles.hasNext()),
                GetArticleResponse.of(slicedArticles.getContent())
        );
    }

    /**
     * [조회수 기준 상위 10개 기사 조회]
     * 1시간 동안 집계된 조회수를 기준으로 상위 N개의 기사를 조회
     *
     * @param topN     상위 N개에 대한 정보
     */
    public List<GetArticleResponse> retrieveTopNArticle(long topN) {
        return rankingService.getRealTimeTopNArticles(topN);
    }

}
