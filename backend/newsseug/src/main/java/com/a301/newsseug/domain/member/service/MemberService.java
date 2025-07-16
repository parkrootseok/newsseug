package com.a301.newsseug.domain.member.service;

import com.a301.newsseug.domain.auth.model.entity.CustomUserDetails;
import com.a301.newsseug.domain.member.model.dto.request.SignUpRequest;
import com.a301.newsseug.domain.member.model.dto.response.GetMemberResponse;
import com.a301.newsseug.domain.member.model.dto.response.SignUpResponse;

public interface MemberService {

    GetMemberResponse retrieveMemberDetails(CustomUserDetails userDetails);

    SignUpResponse signUp(CustomUserDetails userDetails, SignUpRequest request);

}
