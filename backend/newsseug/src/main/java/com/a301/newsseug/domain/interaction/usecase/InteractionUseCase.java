package com.a301.newsseug.domain.interaction.usecase;

import com.a301.newsseug.domain.article.model.dto.ArticleSummaryDto;
import com.a301.newsseug.domain.article.model.dto.response.GetArticleSummaryResponseDto;
import com.a301.newsseug.domain.article.service.ArticleQueryService;
import com.a301.newsseug.domain.auth.model.entity.CustomUserDetails;
import com.a301.newsseug.domain.interaction.model.dto.response.SearchResponse;
import com.a301.newsseug.domain.member.model.entity.Subscribe;
import com.a301.newsseug.domain.member.service.SubscribeService;
import com.a301.newsseug.domain.press.model.dto.PressSummaryDto;
import com.a301.newsseug.domain.press.model.dto.response.GetPressSummaryResponseDto;
import com.a301.newsseug.domain.press.model.entity.Press;
import com.a301.newsseug.domain.press.service.PressCacheManager;
import com.a301.newsseug.domain.press.service.PressQueryService;
import com.a301.newsseug.global.model.dto.SlicedResponse;
import com.a301.newsseug.global.model.entity.SliceDetails;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InteractionUseCase {

    private final ArticleQueryService articleQueryService;
    private final PressQueryService pressQueryService;
    private final PressCacheManager pressCacheManager;
    private final SubscribeService subscribeService;

    public SearchResponse search(CustomUserDetails userDetails, String keyword, int pageNumber) {

        Slice<ArticleSummaryDto> slicedArticles = articleQueryService.getSlicedArticlesByTitle(keyword, pageNumber);
        Map<Long, Press> pressMap = pressCacheManager.getPressMapFromCache(
                slicedArticles.getContent().stream()
                        .map(ArticleSummaryDto::getPressId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toUnmodifiableSet())
        );
        List<PressSummaryDto> pressSummaries = pressQueryService.getPressSummariesByName(keyword);

        if (userDetails.isEnabled()) {
            Set<Press> subscribedPress = new HashSet<>(
                    subscribeService.getSubscribeByMember(userDetails.getMember()).stream()
                            .map(Subscribe::getPress)
                            .toList()
            );

            return SearchResponse.of(
                    GetPressSummaryResponseDto.of(pressSummaries, subscribedPress),
                    SlicedResponse.of(
                            SliceDetails.of(slicedArticles.getNumber(), slicedArticles.isFirst(), slicedArticles.hasNext()),
                            slicedArticles.getContent().stream()
                                    .map(dto -> GetArticleSummaryResponseDto.of(dto, pressMap.get(dto.getPressId())))
                                    .toList()
                    )
            );
        }

        return SearchResponse.of(
                GetPressSummaryResponseDto.of(pressSummaries),
                SlicedResponse.of(
                        SliceDetails.of(slicedArticles.getNumber(), slicedArticles.isFirst(), slicedArticles.hasNext()),
                        slicedArticles.getContent().stream()
                                .map(dto -> GetArticleSummaryResponseDto.of(dto, pressMap.get(dto.getPressId())))
                                .toList()
                )
        );

    }

}
