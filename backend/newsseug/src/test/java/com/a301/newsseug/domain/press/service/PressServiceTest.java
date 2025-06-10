package com.a301.newsseug.domain.press.service;

import static org.assertj.core.api.Assertions.*;

import com.a301.newsseug.domain.auth.model.entity.CustomUserDetails;
import com.a301.newsseug.domain.member.factory.entity.SubscribeFactory;
import com.a301.newsseug.domain.member.model.entity.Subscribe;
import com.a301.newsseug.global.model.entity.ActivationStatus;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.BDDMockito.given;

import com.a301.newsseug.domain.member.factory.entity.MemberFactory;
import com.a301.newsseug.domain.member.model.entity.Member;
import com.a301.newsseug.domain.member.repository.SubscribeRepository;
import com.a301.newsseug.domain.press.exception.NotExistPressException;
import com.a301.newsseug.domain.press.factory.PressFactory;
import com.a301.newsseug.domain.press.model.dto.response.GetPressSummaryResponseDto;
import com.a301.newsseug.domain.press.model.dto.response.GetPressDetailResponseDto;
import com.a301.newsseug.domain.press.model.entity.Press;
import com.a301.newsseug.domain.press.repository.PressRepository;

@DisplayName("언론사 관련 기능")
@ExtendWith(MockitoExtension.class)
public class PressServiceTest {

	@Mock
	private PressRepository pressRepository;

	@Mock
	private SubscribeRepository subscribeRepository;

	@InjectMocks
	private PressServiceImpl pressService;

	@Mock
	private CustomUserDetails userDetails;

	private Member loginMember;

	@BeforeEach
	void beforeEach() {
		loginMember = MemberFactory.memberOfKakao(1L);
		lenient().when(userDetails.getMember()).thenReturn(loginMember);
	}

	@Test
	@DisplayName("언론사 단순 정보 목록 조회")
	void getPress() {
		// Given
		Press press1 = PressFactory.press(0L);
		Press press2 = PressFactory.press(1L);
		List<Subscribe> subscribes = List.of(
				SubscribeFactory.subscribe(0L, press1),
				SubscribeFactory.subscribe(1L, press2)
		);

		given(pressRepository.findAllArticles()).willReturn(List.of(press1, press2));
//		given(subscribeRepository.findAllByMember(loginMember)).willReturn(subscribes);

		// When
		List<GetPressSummaryResponseDto> response = pressService.getPress(userDetails);

		// Then
		assertThat(response).hasSize(2);
		assertThat(response)
			.extracting(GetPressSummaryResponseDto::id, GetPressSummaryResponseDto::name, GetPressSummaryResponseDto::imageUrl, GetPressSummaryResponseDto::isSubscribed)
			.containsExactlyInAnyOrder(
				tuple(
						press1.getId(),
						press1.getName(),
						press1.getImageUrl(),
						true
				),
				tuple(
						press2.getId(),
						press2.getName(),
						press2.getImageUrl(),
						true)
			);

		verify(pressRepository).findAllArticles();

	}

	@Test
	@DisplayName("언론사 단순 정보 목록 조회")
	void getPressEmpty() {

		// Given
		Press press1 = PressFactory.press(0L);
		Press press2 = PressFactory.press(1L);


		given(pressRepository.findAllArticles()).willReturn(List.of(press1, press2));
//		given(subscribeRepository.findAllByMember(loginMember))
//				.willReturn(List.of(SubscribeFactory.subscribe(0L, press1)));

		// When
		List<GetPressSummaryResponseDto> response = pressService.getPress(userDetails);

		// Then
		assertThat(response).hasSize(2);
		assertThat(response)
			.extracting(GetPressSummaryResponseDto::id, GetPressSummaryResponseDto::name, GetPressSummaryResponseDto::imageUrl, GetPressSummaryResponseDto::isSubscribed)
			.containsExactlyInAnyOrder(
				tuple(0L, "name", "imageUrl", true),
				tuple(1L, "name", "imageUrl", false)
			);

		verify(pressRepository).findAllArticles();

	}

	@Test
	@DisplayName("언론사 상세 조회")
	void getPressDetails() {
		// Given
		Press press = PressFactory.press(0L);

		given(pressRepository.findOrThrow(press.getId())).willReturn(press);

		// When
		GetPressDetailResponseDto response = pressService.getPressDetails(userDetails, press.getId());

		// Then
		verify(pressRepository).findOrThrow(press.getId());

		assertThat(response.id()).isEqualTo(press.getId());
		assertThat(response.name()).isEqualTo(press.getName());
		assertThat(response.imageUrl()).isEqualTo(press.getImageUrl());
		assertThat(response.description()).isEqualTo(press.getDescription());
		assertThat(response.isSubscribed()).isFalse();
		assertThat(response.subscribeCount()).isEqualTo(0L);
	}

	@Test
	@DisplayName("존재하지 않는 언론사 상세 조회")
	void getPressNotExistPressDetails() {

		// Given
		Press press = PressFactory.press(0L);


		given(pressRepository.findOrThrow(press.getId())).willThrow(NotExistPressException.class);

		// Then
		assertThatThrownBy(() -> pressService.getPressDetails(userDetails, press.getId())).isInstanceOf(NotExistPressException.class);

	}

	@Test
	@DisplayName("로그인 유저 구독한 언론사 상세 조회")
	void getPressDetailsWithLoginUser() {

		// Given
		Press press = PressFactory.press(0L);
		given(pressRepository.findOrThrow(press.getId())).willReturn(press);
		given(subscribeRepository.existsByMemberAndPressAndActivationStatus(loginMember, press, ActivationStatus.ACTIVE)).willReturn(true);

		// When
		GetPressDetailResponseDto response = pressService.getPressDetails(userDetails, press.getId());

		// Then
		verify(pressRepository).findOrThrow(press.getId());
		verify(subscribeRepository).existsByMemberAndPressAndActivationStatus(loginMember, press, ActivationStatus.ACTIVE);

		assertThat(response.id()).isEqualTo(press.getId());
		assertThat(response.name()).isEqualTo(press.getName());
		assertThat(response.imageUrl()).isEqualTo(press.getImageUrl());
		assertThat(response.description()).isEqualTo(press.getDescription());
		assertThat(response.isSubscribed()).isTrue();
		assertThat(response.subscribeCount()).isEqualTo(0L);

	}

	@Test
	@DisplayName("로그인 유저 구독하지 않은 언론사 상세 조회")
	void getPressDetailsWithLoginUserNotSubscribe() {

		// Given
		Press press = PressFactory.press(0L);
		given(pressRepository.findOrThrow(press.getId())).willReturn(press);
		given(subscribeRepository.existsByMemberAndPressAndActivationStatus(loginMember, press, ActivationStatus.ACTIVE)).willReturn(false);

		// When
		GetPressDetailResponseDto response = pressService.getPressDetails(userDetails, press.getId());

		// Then
		verify(pressRepository).findOrThrow(press.getId());
		verify(subscribeRepository).existsByMemberAndPressAndActivationStatus(loginMember, press, ActivationStatus.ACTIVE);

		assertThat(response.id()).isEqualTo(press.getId());
		assertThat(response.name()).isEqualTo(press.getName());
		assertThat(response.imageUrl()).isEqualTo(press.getImageUrl());
		assertThat(response.description()).isEqualTo(press.getDescription());
		assertThat(response.isSubscribed()).isFalse();
		assertThat(response.subscribeCount()).isEqualTo(0L);

	}

}
