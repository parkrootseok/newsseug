package com.a301.newsseug.domain.press.usecase;

import com.a301.newsseug.domain.auth.model.entity.CustomUserDetails;
import com.a301.newsseug.domain.member.model.entity.Member;
import com.a301.newsseug.domain.member.model.entity.Subscribe;
import com.a301.newsseug.domain.member.service.SubscribeService;
import com.a301.newsseug.domain.press.exception.NotExistPressException;
import com.a301.newsseug.domain.press.factory.PressFactory;
import com.a301.newsseug.domain.press.factory.PressSummaryDtoFactory;
import com.a301.newsseug.domain.press.model.dto.PressSummaryDto;
import com.a301.newsseug.domain.press.model.dto.response.GetPressDetailResponseDto;
import com.a301.newsseug.domain.press.model.dto.response.GetPressSummaryResponseDto;
import com.a301.newsseug.domain.press.model.entity.Press;
import com.a301.newsseug.domain.press.service.PressCacheManager;
import com.a301.newsseug.domain.press.service.PressQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PressUseCaseTest {

    @Mock PressCacheManager pressCacheManager;
    @Mock PressQueryService pressQueryService;
    @Mock SubscribeService subscribeService;
    @Mock CustomUserDetails userDetails;

    @InjectMocks PressUseCase useCase;

    @Test
    @DisplayName("목록 조회 [성공 - 비활성 사용자, 구독정보 없음]")
    void retrievePressSummaries_whenUserDisabled_thenAllUnsubscribed() {
        // given
        when(userDetails.isEnabled()).thenReturn(false);
        List<PressSummaryDto> summaries = List.of(
                PressSummaryDtoFactory.summary(1L, "조선일보"),
                PressSummaryDtoFactory.summary(2L, "한겨레")
        );
        when(pressQueryService.getPressSummaries()).thenReturn(summaries);

        // when
        List<GetPressSummaryResponseDto> result = useCase.retrievePressSummaries(userDetails);

        // then
        verify(pressQueryService).getPressSummaries();
        verifyNoInteractions(subscribeService);
        assertThat(result).hasSize(2);
        assertThat(result).allSatisfy(dto -> assertThat(dto.isSubscribed()).isFalse());
    }

    @Test
    @DisplayName("목록 조회 [성공 - 활성 사용자, 구독목록 결합]")
    void retrievePressSummaries_whenUserEnabled_thenSubscribedMerged() {
        // given
        when(userDetails.isEnabled()).thenReturn(true);
        Member member = mock(Member.class);
        when(userDetails.getMember()).thenReturn(member);

        List<PressSummaryDto> summaries = List.of(
                PressSummaryDtoFactory.summary(1L, "조선일보"),
                PressSummaryDtoFactory.summary(2L, "한겨레")
        );
        when(pressQueryService.getPressSummaries()).thenReturn(summaries);
        Press press1 = PressFactory.press(1L);
        Subscribe sub1 = Subscribe.builder().member(member).press(press1).build();
        when(subscribeService.getSubscribeByMember(member)).thenReturn(List.of(sub1));

        // when
        List<GetPressSummaryResponseDto> result = useCase.retrievePressSummaries(userDetails);

        // then
        verify(subscribeService).getSubscribeByMember(member);
        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting(GetPressSummaryResponseDto::id, GetPressSummaryResponseDto::isSubscribed)
                .containsExactlyInAnyOrder(tuple(1L, true), tuple(2L, false));
    }

    @Test
    @DisplayName("상세 조회 [성공 - 활성 사용자, 구독여부 확인]")
    void retrievePressDetail_whenUserEnabled_thenCheckSubscribed() {
        // given
        when(userDetails.isEnabled()).thenReturn(true);
        Member member = mock(Member.class);
        when(userDetails.getMember()).thenReturn(member);

        Press press = PressFactory.press(10L);
        when(pressCacheManager.getCachedPress(10L)).thenReturn(press);
        when(subscribeService.isSubscribed(member, press)).thenReturn(true);

        // when
        GetPressDetailResponseDto dto = useCase.retrievePressDetail(userDetails, 10L);

        // then
        verify(pressCacheManager).getCachedPress(10L);
        verify(subscribeService).isSubscribed(member, press);
        assertThat(dto.isSubscribed()).isTrue();
    }

    @Test
    @DisplayName("상세 조회 [성공 - 비활성 사용자, 구독여부 조회 없음]")
    void retrievePressDetail_whenUserDisabled_thenSkipSubscribedCheck() {
        // given
        when(userDetails.isEnabled()).thenReturn(false);

        Press press = PressFactory.press(20L);
        when(pressCacheManager.getCachedPress(20L)).thenReturn(press);

        // when
        GetPressDetailResponseDto dto = useCase.retrievePressDetail(userDetails, 20L);

        // then
        verify(pressCacheManager).getCachedPress(20L);
        verify(subscribeService, never()).isSubscribed(any(), any());
        assertThat(dto.isSubscribed()).isFalse();
    }

    @Test
    @DisplayName("상세 조회 [실패 - 존재하지 않는 ID]")
    void retrievePressDetail_whenNotFound_thenThrowException() {
        // given
        when(pressCacheManager.getCachedPress(999L))
                .thenThrow(NotExistPressException.class);

        // when / then
        assertThatThrownBy(() -> useCase.retrievePressDetail(userDetails, 999L))
                .isInstanceOf(NotExistPressException.class);

        verify(subscribeService, never()).isSubscribed(any(), any());
    }

    @Test
    @DisplayName("목록 조회 [성공 - 전체 목록이 비어있을 때도 빈 리스트 반환]")
    void retrievePressSummaries_whenEmpty_thenReturnEmptyList() {
        // given
        when(userDetails.isEnabled()).thenReturn(false);
        when(pressQueryService.getPressSummaries()).thenReturn(List.of());

        // when
        List<GetPressSummaryResponseDto> result = useCase.retrievePressSummaries(userDetails);

        // then
        assertThat(result).isEmpty();
        verifyNoInteractions(subscribeService);
    }

    @Test
    @DisplayName("목록 조회 [성공 - 활성 사용자지만 구독 0개]")
    void retrievePressSummaries_whenSubscribedListEmpty_thenAllFalse() {
        // given
        when(userDetails.isEnabled()).thenReturn(true);
        Member member = mock(Member.class);
        when(userDetails.getMember()).thenReturn(member);

        when(pressQueryService.getPressSummaries()).thenReturn(
                List.of(
                        PressSummaryDtoFactory.summary(1L, "A"),
                        PressSummaryDtoFactory.summary(2L, "B")
                )
        );
        when(subscribeService.getSubscribeByMember(member)).thenReturn(List.of());

        // when
        List<GetPressSummaryResponseDto> result = useCase.retrievePressSummaries(userDetails);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).allSatisfy(dto -> assertThat(dto.isSubscribed()).isFalse());
    }

    @Test
    @DisplayName("목록 조회 [성공 - 구독 목록에 전체 목록에 없는 언론사가 섞여 있어도 안전]")
    void retrievePressSummaries_whenSubscribedNotInSummary_thenIgnore() {
        // given
        when(userDetails.isEnabled()).thenReturn(true);
        Member member = mock(Member.class);
        when(userDetails.getMember()).thenReturn(member);

        List<PressSummaryDto> summaries = List.of(
                PressSummaryDtoFactory.summary(1L, "조선일보")
        );
        when(pressQueryService.getPressSummaries()).thenReturn(summaries);

        Press ghostPress = PressFactory.press(999L);
        Subscribe ghostSub = Subscribe.builder().member(member).press(ghostPress).build();
        when(subscribeService.getSubscribeByMember(member)).thenReturn(List.of(ghostSub));

        // when
        List<GetPressSummaryResponseDto> result = useCase.retrievePressSummaries(userDetails);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).isSubscribed()).isFalse();
    }
}
