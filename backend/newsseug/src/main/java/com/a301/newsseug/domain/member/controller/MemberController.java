package com.a301.newsseug.domain.member.controller;

import com.a301.newsseug.domain.auth.model.entity.CustomUserDetails;
import com.a301.newsseug.domain.folder.model.dto.response.GetFolderResponse;
import com.a301.newsseug.domain.folder.service.FolderService;
import com.a301.newsseug.domain.member.model.dto.request.UpdateMemberRequest;
import com.a301.newsseug.domain.member.model.dto.response.GetMemberResponse;
import com.a301.newsseug.domain.member.service.MemberService;
import com.a301.newsseug.domain.press.model.dto.response.ListSimplePressResponse;
import com.a301.newsseug.global.model.dto.Result;
import com.a301.newsseug.global.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "멤버 API")
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final FolderService folderService;

    @Operation(summary = "사용자 정보 조회 API", description = "사용자 정보를 조회한다.")
    @GetMapping()
    public ResponseEntity<Result<GetMemberResponse>> updateMember(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseUtil.ok(Result.of(memberService.getMember(userDetails)));
    }

    @Operation(summary = "사용자 정보 등록 API", description = "사용자 정보를 등록한다.")
    @PutMapping()
    public ResponseEntity<Result<Boolean>> updateMember(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid UpdateMemberRequest request
    ) {

        memberService.updateMember(userDetails, request);
        return ResponseUtil.ok(Result.of(Boolean.TRUE));

    }

    @Operation(summary = "사용자 폴더 목록 조회 API", description = "사용자 폴더 목록을 조회한다.")
    @GetMapping("/folders")
    public ResponseEntity<Result<List<GetFolderResponse>>> getFolders(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        return ResponseUtil.ok(
                Result.of(folderService.getFoldersByMember(userDetails))
        );

    }


    @Operation(summary = "구독한 언론사 목록 조회", description = "사용자가 구독한 언론사 목록을 조회한다.")
    @GetMapping("/press")
    public ResponseEntity<Result<ListSimplePressResponse>> getPress(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        return ResponseUtil.ok(
                Result.of(memberService.getPressByMember(userDetails))
        );

    }

    @Operation(summary = "언론사 구독", description = "사용자가 언론사를 구독한다.")
    @PostMapping("/press/{pressId}")
    public ResponseEntity<Result<Boolean>> subscribePress(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable(name = "pressId") Long pressId
    ) {
        memberService.subscribe(userDetails, pressId);
        return ResponseUtil.ok(
                Result.of(Boolean.TRUE)
        );

    }

    @Operation(summary = "언론사 구독 해제", description = "사용자가 언론사 구독을 해제한다.")
    @PutMapping("/press/{pressId}")
    public ResponseEntity<Result<Boolean>> unsubscribePress(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable(name = "pressId") Long pressId
    ) {
        memberService.unsubscribe(userDetails, pressId);
        return ResponseUtil.ok(
                Result.of(Boolean.TRUE)
        );

    }
}
