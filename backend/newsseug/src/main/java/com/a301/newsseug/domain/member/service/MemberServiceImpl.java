package com.a301.newsseug.domain.member.service;

import com.a301.newsseug.domain.auth.model.entity.CustomUserDetails;
import com.a301.newsseug.domain.member.exception.DuplicateNicknameException;
import com.a301.newsseug.domain.member.model.dto.request.SignUpRequest;
import com.a301.newsseug.domain.member.model.dto.response.GetMemberResponse;
import com.a301.newsseug.domain.member.model.dto.response.SignUpResponse;
import com.a301.newsseug.domain.member.model.entity.type.GenderType;
import com.a301.newsseug.domain.member.model.entity.Member;
import com.a301.newsseug.domain.member.repository.MemberRepository;
import io.jsonwebtoken.lang.Strings;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private static final DateTimeFormatter BIRTH_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public GetMemberResponse retrieveMemberDetails(CustomUserDetails userDetails) {
        return GetMemberResponse.of(userDetails.getMember());
    }

    @Override
    public SignUpResponse signUp(CustomUserDetails userDetails, SignUpRequest request) {
        Member member = userDetails.getMember();
        if (Strings.hasText(request.birth())) {
            member.setBirth(LocalDate.parse(request.birth(), BIRTH_FORMATTER));
        }
        if (Strings.hasText(request.gender())) {
            member.setGender(GenderType.from(request.gender()));
        }
        if (Strings.hasText(request.nickname())) {
            if (!request.nickname().equals(member.getNickname()) && memberRepository.existsByNickname(request.nickname())) {
                throw new DuplicateNicknameException();
            }
            member.setNickname(request.nickname());
        }
        if (Strings.hasText(request.profileImageUrl())) {
            member.setProfileImageUrl(request.profileImageUrl());
        }
        member.promoteToMember();
        return SignUpResponse.of(member.getId());
    }

}
