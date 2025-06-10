package com.a301.newsseug.domain.article.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.a301.newsseug.domain.article.model.dto.response.GetArticleSummaryResponseDto;
import com.a301.newsseug.domain.article.model.dto.response.*;
import com.a301.newsseug.domain.article.usecase.ArticleUseCase;
import com.a301.newsseug.domain.auth.model.entity.CustomUserDetails;
import com.a301.newsseug.domain.interaction.controller.ReactionController;
import com.a301.newsseug.domain.auth.annotation.NullableUserDetails;
import com.a301.newsseug.global.model.dto.Result;
import com.a301.newsseug.global.model.dto.SlicedResponse;
import com.a301.newsseug.global.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "기사 API")
@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleUseCase useCase;

    @Operation(summary = "단일 기사 상세 정보 조회 API")
    @GetMapping("/{articleId}")
    public ResponseEntity<EntityModel<Result<GetArticleDetailResponseDto>>> getArticle(
            @NullableUserDetails CustomUserDetails userDetails,
            @PathVariable(name = "articleId") Long articleId
   ) {
        GetArticleDetailResponseDto response = useCase.retrieveArticleDetails(userDetails, articleId);
        EntityModel<Result<GetArticleDetailResponseDto>> model = EntityModel.of(
                Result.of(response),
                linkTo(methodOn(ArticleController.class).getArticle(userDetails, articleId)).withSelfRel(),
                linkTo(methodOn(ReactionController.class).doReactionToArticle(userDetails, articleId, "like")).withRel("do-like"),
                linkTo(methodOn(ReactionController.class).cancelReactionToArticle(userDetails, articleId, "like")).withRel("cancel-like"),
                linkTo(methodOn(ReactionController.class).doReactionToArticle(userDetails, articleId, "hate")).withRel("do-hate"),
                linkTo(methodOn(ReactionController.class).cancelReactionToArticle(userDetails, articleId, "hate")).withRel("cancel-hate")
        );
        return ResponseUtil.ok(model);
    }

    @Operation(summary = "카테고리별 기사 목록 조회 API")
    @GetMapping
    public ResponseEntity<EntityModel<Result<SlicedResponse<List<GetArticleSummaryResponseDto>>>>> getArticlesByCategory(
            @RequestParam(required = false, defaultValue = "ALL", value = "category") String category,
            @RequestParam(required = false, defaultValue = "0", value = "pageNumber") int pageNumber

    ) {
        return ResponseUtil.ok(
                Result.of(useCase.retrieveArticlesByCategory(category, pageNumber))
        );
    }

    @Operation(summary = "실시간 인기 뉴스 조회 API", description = "1시간 동안 쌓인 조회수를 기준으로 오름차순 정렬한 상위 10개의 기사를 조회한다.")
    @GetMapping("/ranking")
    public ResponseEntity<EntityModel<Result<List<GetArticleSummaryResponseDto>>>> retrieveTop10Article(
            @RequestParam(required = false, defaultValue = "10", value = "topN") long topN
    ) {
        return ResponseUtil.ok(Result.of(useCase.retrieveTopNArticles(topN)));
    }

    @Operation(summary = "오늘의 뉴스 조회 API", description = "\"오늘의 뉴스\"를 조회한다.")
    @GetMapping("/today")
    public ResponseEntity<EntityModel<Result<SlicedResponse<List<GetArticleSummaryResponseDto>>>>> getTodaysArticles(
            @RequestParam(required = false, defaultValue = "ALL", value = "filter") String filter,
            @RequestParam(required = false, defaultValue = "0", value = "pageNumber") int pageNumber
    ) {
        return ResponseUtil.ok(Result.of(useCase.retrieveTodayArticles(filter, pageNumber)));
    }

    @Operation(summary = "언론사별 기사 조회 API")
    @GetMapping(value = "/press/{pressId}")
    public ResponseEntity<EntityModel<Result<SlicedResponse<List<GetArticleSummaryResponseDto>>>>> getArticlesByPress(
            @PathVariable(required = false, name = "pressId") Long pressId,
            @RequestParam(required = false, defaultValue = "ALL", value = "filter") String filter,
            @RequestParam(required = false, defaultValue = "0", value = "pageNumber") int pageNumber
    ) {
        return ResponseUtil.ok(
                Result.of(useCase.retrieveArticlesByPress(pressId, filter, pageNumber))
        );
    }

    @Operation(summary = "구독한 언론사 기사 조회 API")
    @GetMapping(value = "/press")
    public ResponseEntity<EntityModel<Result<SlicedResponse<List<GetArticleSummaryResponseDto>>>>> getSubscribedArticles(
            @NullableUserDetails CustomUserDetails userDetails,
            @RequestParam(required = false, defaultValue = "ALL", value = "filter") String filter,
            @RequestParam(required = false, defaultValue = "0", value = "pageNumber") int pageNumber
    ) {
        return ResponseUtil.ok(
                Result.of(useCase.retrieveArticlesBySubscribedPress(userDetails, filter, pageNumber))
        );
    }

}
