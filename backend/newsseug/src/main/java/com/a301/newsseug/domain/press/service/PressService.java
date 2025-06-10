package com.a301.newsseug.domain.press.service;

import com.a301.newsseug.domain.auth.model.entity.CustomUserDetails;
import com.a301.newsseug.domain.press.model.dto.response.GetPressDetailResponseDto;
import com.a301.newsseug.domain.press.model.dto.response.GetPressSummaryResponseDto;
import java.util.List;

public interface PressService {

	List<GetPressSummaryResponseDto> getPress(CustomUserDetails userDetails);

	GetPressDetailResponseDto getPressDetails(CustomUserDetails userDetails, Long pressId);

}
