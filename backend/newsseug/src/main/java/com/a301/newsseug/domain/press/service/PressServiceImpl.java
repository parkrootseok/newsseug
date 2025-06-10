package com.a301.newsseug.domain.press.service;

import com.a301.newsseug.domain.member.model.entity.Subscribe;
import com.a301.newsseug.domain.member.service.SubscribeService;
import com.a301.newsseug.domain.press.model.dto.PressSummaryDto;
import com.a301.newsseug.domain.press.model.dto.response.GetPressSummaryResponseDto;
import java.util.HashSet;
import java.util.List;

import com.a301.newsseug.domain.auth.model.entity.CustomUserDetails;
import com.a301.newsseug.domain.press.model.dto.response.GetPressDetailResponseDto;
import com.a301.newsseug.domain.press.model.entity.Press;
import com.a301.newsseug.domain.press.repository.PressRepository;

import java.util.Set;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PressServiceImpl implements PressService {

    private final PressCacheManager pressCacheManager;
    private final SubscribeService subscribeService;
    private final PressRepository pressRepository;

    @Override
    public List<GetPressSummaryResponseDto> getPress(CustomUserDetails userDetails) {
        List<PressSummaryDto> press = pressRepository.findAllArticles();
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

    @Override
    public GetPressDetailResponseDto getPressDetails(CustomUserDetails userDetails, Long pressId) {
        Press press = pressCacheManager.getCachedPress(pressId);
        if (userDetails.isEnabled()) {
            return  GetPressDetailResponseDto.of(press, subscribeService.isSubscribed(userDetails.getMember(), press));
        }
        return GetPressDetailResponseDto.of(press);
    }

}
