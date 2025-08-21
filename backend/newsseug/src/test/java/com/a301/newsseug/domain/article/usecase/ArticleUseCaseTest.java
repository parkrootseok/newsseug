package com.a301.newsseug.domain.article.usecase;

import com.a301.newsseug.domain.article.factory.ArticleFactory;
import com.a301.newsseug.domain.article.factory.ArticleSummaryDtoFactory;
import com.a301.newsseug.domain.article.model.dto.ArticleSummaryDto;
import com.a301.newsseug.domain.article.model.dto.response.GetArticleDetailResponseDto;
import com.a301.newsseug.domain.article.model.dto.response.GetArticleSummaryResponseDto;
import com.a301.newsseug.domain.article.model.entity.Article;
import com.a301.newsseug.domain.article.model.entity.type.CategoryType;
import com.a301.newsseug.domain.article.service.ArticleCacheManager;
import com.a301.newsseug.domain.article.service.ArticleCountingService;
import com.a301.newsseug.domain.article.service.ArticleQueryService;
import com.a301.newsseug.domain.article.service.ArticleRankingService;
import com.a301.newsseug.domain.auth.model.entity.CustomUserDetails;
import com.a301.newsseug.domain.interaction.service.HistoryService;
import com.a301.newsseug.domain.member.factory.MemberFactory;
import com.a301.newsseug.domain.member.factory.SubscribeFactory;
import com.a301.newsseug.domain.member.model.entity.Member;
import com.a301.newsseug.domain.member.model.entity.Subscribe;
import com.a301.newsseug.domain.member.service.SubscribeService;
import com.a301.newsseug.domain.press.factory.PressFactory;
import com.a301.newsseug.domain.press.model.entity.Press;
import com.a301.newsseug.domain.press.service.PressCacheManager;
import com.a301.newsseug.domain.reaction.model.entity.type.ReactionType;
import com.a301.newsseug.domain.reaction.service.ReactionService;
import com.a301.newsseug.external.oauth.model.entity.type.ProviderType;
import com.a301.newsseug.global.model.dto.SlicedResponse;
import com.a301.newsseug.global.model.entity.SliceDetails;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleUseCaseTest {

    @InjectMocks
    private ArticleUseCase useCase;

    @Mock private ArticleCacheManager articleCacheManager;
    @Mock private ArticleQueryService articleRetrieveService;
    @Mock private ArticleCountingService articleCountingService;
    @Mock private ArticleRankingService articleRankingService;
    @Mock private PressCacheManager pressCacheManager;
    @Mock private ReactionService reactionService;
    @Mock private SubscribeService subscribeService;
    @Mock private HistoryService historyService;
    @Mock private ArticleQueryService articleQueryService;

    @Mock private CustomUserDetails userDetails;

    @Test
    @DisplayName("단일 기사 조회 [성공 - 비활성 사용자: 구독/리액션/히스토리 비호출, 집계만 수행]")
    void retrieveArticleDetails_whenUserDisabled_thenAggregationsOnly() {
        // given
        Press press = PressFactory.press(1L, "연합뉴스");
        Article article = ArticleFactory.createDefault(press);

        when(userDetails.isEnabled()).thenReturn(false);
        when(articleCacheManager.getCachedArticle(100L)).thenReturn(article);
        when(articleCountingService.increaseViewCount(100L)).thenReturn(5L);
        when(articleCountingService.getLikeCount(100L)).thenReturn(2L);
        when(articleCountingService.getHateCount(100L)).thenReturn(1L);

        // when
        GetArticleDetailResponseDto result = useCase.retrieveArticleDetails(userDetails, 100L);

        // then
        verify(articleCacheManager).getCachedArticle(100L);
        verify(articleRankingService).increaseRanking(100L, 1);
        verify(articleCountingService).increaseViewCount(100L);
        verify(articleCountingService).getLikeCount(100L);
        verify(articleCountingService).getHateCount(100L);
        verifyNoInteractions(subscribeService, reactionService, historyService);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("단일 기사 조회 [성공 - 활성 사용자: 히스토리/구독/리액션 포함 호출]")
    void retrieveArticleDetails_whenUserEnabled_thenHistorySubscribeReactionsCalled() {
        // given
        Press press = PressFactory.press(2L, "조선일보");
        Article article = ArticleFactory.createDefault(press);
        Member member = MemberFactory.member(ProviderType.GOOGLE, "google-uid");

        when(userDetails.isEnabled()).thenReturn(true);
        when(userDetails.getMember()).thenReturn(member);
        when(articleCacheManager.getCachedArticle(200L)).thenReturn(article);
        when(articleCountingService.increaseViewCount(200L)).thenReturn(3L);
        when(articleCountingService.getLikeCount(200L)).thenReturn(10L);
        when(articleCountingService.getHateCount(200L)).thenReturn(4L);
        when(subscribeService.isSubscribed(member, press)).thenReturn(true);
        when(reactionService.hasReacted(member, article, ReactionType.LIKE)).thenReturn(true);
        when(reactionService.hasReacted(member, article, ReactionType.HATE)).thenReturn(false);

        // when
        GetArticleDetailResponseDto result = useCase.retrieveArticleDetails(userDetails, 200L);

        // then
        verify(historyService).createHistory(member, article);
        verify(subscribeService).isSubscribed(member, press);
        verify(reactionService).hasReacted(member, article, ReactionType.LIKE);
        verify(reactionService).hasReacted(member, article, ReactionType.HATE);
        verify(articleRankingService).increaseRanking(200L, 1);
        verify(articleCountingService).increaseViewCount(200L);
        verify(articleCountingService).getLikeCount(200L);
        verify(articleCountingService).getHateCount(200L);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("카테고리별 기사 조회 [성공 - 카테고리 지정]")
    void retrieveArticlesByCategory_whenCategoryProvided_thenMappedWithPress() {
        // given
        ArticleSummaryDto dto1 = ArticleSummaryDtoFactory.summaryWithPress(11L, "경제1", 10L);
        ArticleSummaryDto dto2 = ArticleSummaryDtoFactory.summaryWithPress(12L, "경제2", 20L);

        Slice<ArticleSummaryDto> slice = new SliceImpl<>(
                List.of(dto1, dto2),
                PageRequest.of(0, 2),
                true
        );

        when(articleRetrieveService.getSlicedArticlesByCategory(CategoryType.ECONOMY, 0))
                .thenReturn(slice);

        Press p1 = PressFactory.press(10L, "한겨레");
        Press p2 = PressFactory.press(20L, "경향신문");
        Map<Long, Press> pressMap = Map.of(10L, p1, 20L, p2);

        ArgumentCaptor<Set<Long>> setCaptor = ArgumentCaptor.forClass(Set.class);
        when(pressCacheManager.getPressMapFromCache(anySet())).thenReturn(pressMap);

        // when
        SlicedResponse<List<GetArticleSummaryResponseDto>> result =
                useCase.retrieveArticlesByCategory("ECONOMY", 0);

        // then
        verify(articleRetrieveService).getSlicedArticlesByCategory(CategoryType.ECONOMY, 0);
        verify(pressCacheManager).getPressMapFromCache(setCaptor.capture());

        assertThat(setCaptor.getValue()).containsExactlyInAnyOrder(10L, 20L);

        assertThat(result.getContent()).hasSize(2);
        SliceDetails details = result.getSliceDetails();
        assertThat(details.isHasNext()).isTrue();
        assertThat(details.getCurrentPage()).isEqualTo(0);
    }

    @Test
    @DisplayName("오늘의 뉴스 조회 [성공 - Press 캐시 조회 2회 호출 확인]")
    void retrieveTodayArticles_whenCalled_thenPressCacheLookupsHappen() {
        // given
        ArticleSummaryDto dto1 = ArticleSummaryDtoFactory.summaryWithPress(21L, "정치1", 30L);
        ArticleSummaryDto dto2 = ArticleSummaryDtoFactory.summaryWithPress(22L, "정치2", 40L);

        Slice<ArticleSummaryDto> slice = new SliceImpl<>(
                List.of(dto1, dto2),
                PageRequest.of(1, 2),
                false
        );

        when(articleRetrieveService.getSlicedTodayArticlesByCategory(CategoryType.POLITICS, 1))
                .thenReturn(slice);

        Press p1 = PressFactory.press(30L, "MBN");
        Press p2 = PressFactory.press(40L, "SBS");
        Map<Long, Press> pressMap = Map.of(30L, p1, 40L, p2);

        when(pressCacheManager.getPressMapFromCache(Set.of(30L, 40L))).thenReturn(pressMap);
        when(pressCacheManager.getPressMapFromCache(Set.of(30L, 40L))).thenReturn(pressMap);

        // when
        SlicedResponse<List<GetArticleSummaryResponseDto>> result =
                useCase.retrieveTodayArticles("POLITICS", 1);

        // then
        verify(pressCacheManager, times(2)).getPressMapFromCache(Set.of(30L, 40L));
        verify(articleRetrieveService).getSlicedTodayArticlesByCategory(CategoryType.POLITICS, 1);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getSliceDetails().isHasNext()).isFalse();
        assertThat(result.getSliceDetails().getCurrentPage()).isEqualTo(1);
    }

    @Test
    @DisplayName("언론사별 기사 조회 [성공]")
    void retrieveArticlesByPress_whenCalled_thenMappedWithSinglePress() {
        // given
        Press press = PressFactory.press(77L, "KBS");
        when(pressCacheManager.getCachedPress(77L)).thenReturn(press);

        ArticleSummaryDto dto1 = ArticleSummaryDtoFactory.summary(31L, "사회1");
        ArticleSummaryDto dto2 = ArticleSummaryDtoFactory.summary(32L, "사회2");

        Slice<ArticleSummaryDto> slice = new SliceImpl<>(
                List.of(dto1, dto2),
                PageRequest.of(0, 2),
                false
        );
        when(articleRetrieveService.getSlicedArticlesByPressAndCategory(press, CategoryType.SOCIETY, 0))
                .thenReturn(slice);

        // when
        SlicedResponse<List<GetArticleSummaryResponseDto>> result =
                useCase.retrieveArticlesByPress(77L, "SOCIETY", 0);

        // then
        verify(pressCacheManager).getCachedPress(77L);
        verify(articleRetrieveService).getSlicedArticlesByPressAndCategory(press, CategoryType.SOCIETY, 0);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getSliceDetails().isHasNext()).isFalse();
    }

    @Test
    @DisplayName("구독한 언론사 기사 조회 [성공 - 구독 목록 기반 조회]")
    void retrieveArticlesBySubscribedPress_whenDuplicatePress_thenMergeLambdaIsExecuted() {
        // given
        Member member = MemberFactory.member(ProviderType.KAKAO, "kakao-uid");
        when(userDetails.getMember()).thenReturn(member);

        // 같은 Press 인스턴스를 두 번 구독한 케이스 (중복 key 발생)
        Press p1 = PressFactory.press(901L, "조선일보");

        Subscribe s1 = SubscribeFactory.subscribe(member, p1);
        Subscribe s2 = SubscribeFactory.subscribe(member, p1); // 동일 press로 중복

        when(subscribeService.getSubscribeByMember(member)).thenReturn(List.of(s1, s2));

        // DTO들도 동일 pressId로 맞춰서 반환
        ArticleSummaryDto dto1 = ArticleSummaryDtoFactory.summaryWithPress(41L, "경제A", 901L);
        ArticleSummaryDto dto2 = ArticleSummaryDtoFactory.summaryWithPress(42L, "경제B", 901L);

        Slice<ArticleSummaryDto> slice = new SliceImpl<>(
                List.of(dto1, dto2),
                PageRequest.of(2, 2),
                true
        );

        // articleQueryService는 중복 Press 리스트를 그대로 받게 됨 (List.of(p1, p1))
        when(articleQueryService.getSlicedArticlesBySubscribedPress(
                List.of(p1, p1), CategoryType.ECONOMY, 2)).thenReturn(slice);

        // when
        SlicedResponse<List<GetArticleSummaryResponseDto>> result =
                useCase.retrieveArticlesBySubscribedPress(userDetails, "ECONOMY", 2);

        // then
        verify(subscribeService).getSubscribeByMember(member);
        verify(articleQueryService).getSlicedArticlesBySubscribedPress(List.of(p1, p1), CategoryType.ECONOMY, 2);

        // 결과 검증 (pressMap이 중복 키를 병합했더라도 매핑은 정상)
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getSliceDetails().isHasNext()).isTrue();
        assertThat(result.getSliceDetails().getCurrentPage()).isEqualTo(2);
    }

    @Test
    @DisplayName("조회수 기준 상위 N 기사 조회 [성공 - 위임 검증]")
    void retrieveTopNArticles_whenCalled_thenDelegatesToRankingService() {
        // given
        List<GetArticleSummaryResponseDto> top = List.of(mock(GetArticleSummaryResponseDto.class));
        when(articleRankingService.getRealTimeTopNArticles(10L)).thenReturn(top);

        // when
        List<GetArticleSummaryResponseDto> result = useCase.retrieveTopNArticles(10L);

        // then
        verify(articleRankingService).getRealTimeTopNArticles(10L);
        assertThat(result).hasSize(1);
    }

}
