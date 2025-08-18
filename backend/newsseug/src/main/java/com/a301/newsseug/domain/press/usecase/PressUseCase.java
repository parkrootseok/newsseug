package com.a301.newsseug.domain.press.usecase;

import com.a301.newsseug.domain.auth.model.entity.CustomUserDetails;
import com.a301.newsseug.domain.member.model.entity.Subscribe;
import com.a301.newsseug.domain.member.service.SubscribeService;
import com.a301.newsseug.domain.press.model.dto.PressSummaryDto;
import com.a301.newsseug.domain.press.model.dto.response.GetPressDetailResponseDto;
import com.a301.newsseug.domain.press.model.dto.response.GetPressSummaryResponseDto;
import com.a301.newsseug.domain.press.model.entity.Press;
import com.a301.newsseug.domain.press.service.PressCacheManager;
import com.a301.newsseug.domain.press.service.PressQueryService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PressUseCase {

    private final PressCacheManager pressCacheManager;
    private final PressQueryService pressQueryService;
    private final SubscribeService subscribeService;

    public List<GetPressSummaryResponseDto> retrievePressSummaries(CustomUserDetails userDetails) {
        List<PressSummaryDto> press = pressQueryService.getPressSummaries();
        if (userDetails.isEnabled()) {
            Set<Press> subscribedPress = new HashSet<>(
                    subscribeService.getSubscribeByMember(userDetails.getMember()).stream()
                            .map(Subscribe::getPress)
                            .toList()
            );
            return GetPressSummaryResponseDto.of(press, subscribedPress);
        }
        return GetPressSummaryResponseDto.of(press);
    }

    public GetPressDetailResponseDto retrievePressDetail(CustomUserDetails userDetails, Long pressId) {
        Press press = pressCacheManager.getCachedPress(pressId);
        if (userDetails.isEnabled()) {
            return GetPressDetailResponseDto.of(
                    press, subscribeService.isSubscribed(userDetails.getMember(), press)
            );
        }
        return GetPressDetailResponseDto.of(press);
    }

}
