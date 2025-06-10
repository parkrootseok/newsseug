package com.a301.newsseug.domain.interaction.controller;

import com.a301.newsseug.domain.auth.model.entity.CustomUserDetails;
import com.a301.newsseug.domain.interaction.model.dto.response.SearchResponse;
import com.a301.newsseug.domain.interaction.usecase.InteractionUseCase;
import com.a301.newsseug.domain.auth.annotation.NullableUserDetails;
import com.a301.newsseug.global.model.dto.Result;
import com.a301.newsseug.global.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "검색 API")
@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final InteractionUseCase interactionUseCase;

    @Operation(summary = "검색", description = "키워드를 포함하는 언론사, 기사를 조회한다.")
    @GetMapping
    public ResponseEntity<EntityModel<Result<SearchResponse>>> search(
            @NullableUserDetails CustomUserDetails userDetails,
            @RequestParam @NotBlank String keyword,
            @RequestParam(required = false, defaultValue = "0") int pageNumber
    ) {
        return ResponseUtil.ok(
                Result.of(interactionUseCase.search(userDetails, keyword, pageNumber))
        );
    }

}
