package com.a301.newsseug.domain.member.service;

import com.a301.newsseug.domain.auth.model.entity.CustomUserDetails;
import com.a301.newsseug.domain.member.model.dto.request.UpdateMemberRequest;
import com.a301.newsseug.domain.member.model.dto.response.GetMemberResponse;

public interface MemberService {

    GetMemberResponse getMember(CustomUserDetails userDetails);

    void updateMember(CustomUserDetails userDetails, UpdateMemberRequest request);

}
