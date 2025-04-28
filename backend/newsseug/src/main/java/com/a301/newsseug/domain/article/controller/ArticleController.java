package com.a301.newsseug.domain.article.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.a301.newsseug.domain.article.model.dto.response.*;
import com.a301.newsseug.domain.article.service.ArticleService;
import com.a301.newsseug.domain.article.usecase.ArticleUseCase;
import com.a301.newsseug.domain.auth.model.entity.CustomUserDetails;
import com.a301.newsseug.domain.interaction.controller.HateController;
import com.a301.newsseug.domain.interaction.controller.LikeController;
import com.a301.newsseug.global.annotation.NullableUserDetails;
import com.a301.newsseug.global.model.dto.Result;
import com.a301.newsseug.global.model.dto.SlicedResponse;
import com.a301.newsseug.global.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "기사 API")
@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleUseCase useCase;

    @Operation(summary = "단일 기사 상세 정보 조회 API", description = "단일 기사 상세 정보를 조회한다.")
    @GetMapping("/{articleId}")
    public ResponseEntity<EntityModel<Result<GetArticleDetailsResponse>>> getArticle(
            @NullableUserDetails CustomUserDetails userDetails,
            @PathVariable(name = "articleId") Long articleId
   ) {
        GetArticleDetailsResponse response = articleService.getArticleDetail(userDetails, articleId);
        EntityModel<Result<GetArticleDetailsResponse>> model = EntityModel.of(
                Result.of(response),
                linkTo(methodOn(ArticleController.class).getArticle(userDetails, articleId)).withSelfRel(),
                linkTo(methodOn(LikeController.class).createLike(userDetails, articleId)).withRel("like-create"),
                linkTo(methodOn(LikeController.class).deleteLike(userDetails, articleId)).withRel("like-delete"),
                linkTo(methodOn(HateController.class).createHate(userDetails, articleId)).withRel("hate-create"),
                linkTo(methodOn(HateController.class).deleteHate(userDetails, articleId)).withRel("hate-delete")
        );
        return ResponseUtil.ok(model);
    }

    @Operation(summary = "전체 기사 조회 API", description = "전체 기사 리스트를 조회한다.")
    @GetMapping
    public ResponseEntity<EntityModel<Result<SlicedResponse<List<GetArticleResponse>>>>> getArticlesByCategory(
            @RequestParam(required = false, defaultValue = "0", value = "pageNumber") int pageNumber,
            @RequestParam(required = false, defaultValue = "ALL", value = "filter") String filter

    ) {
        return ResponseUtil.ok(
                Result.of(
                        useCase.retrieveAllArticle(filter, pageNumber)
                )
        );
    }

    @Operation(summary = "오늘의 뉴스 조회 API", description = "\"오늘의 뉴스\"를 조회한다.")
    @GetMapping("/today")
    public ResponseEntity<EntityModel<Result<SlicedResponse<List<GetArticleResponse>>>>> getTodaysArticles(
            @RequestParam(required = false, defaultValue = "0", value = "pageNumber") int pageNumber,
            @RequestParam(required = false, defaultValue = "ALL", value = "filter") String filter
    ) {
        return ResponseUtil.ok(Result.of(articleService.getTodayArticlesByCategory(filter, pageNumber)));
    }

    @Operation(summary = "랜덤 기사 조회 API", description = "랜덤 기사 리스트를 조회한다.")
    @GetMapping("/random")
    public ResponseEntity<EntityModel<Result<SlicedResponse<List<GetArticleResponse>>>>> getRandomArticle(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseUtil.ok(
                Result.of(articleService.getRandomArticle(userDetails))
        );
    }

    @Operation(summary = "언론사별 기사 조회 API", description = "언론사별 기사 리스트를 조회한다.")
    @GetMapping(value = {"/press", "/press/{pressId}"})
    public ResponseEntity<EntityModel<Result<SlicedResponse<List<GetArticleResponse>>>>> getArticlesByPress(
            @NullableUserDetails CustomUserDetails userDetails,
            @PathVariable(required = false, name = "pressId") Long pressId,
            @RequestParam(required = false, defaultValue = "0", value = "pageNumber") int pageNumber,
            @RequestParam(required = false, defaultValue = "ALL", value = "filter") String filter
    ) {
        return ResponseUtil.ok(
                Result.of(articleService.getArticlesByPress(userDetails, pressId, pageNumber, filter))
        );
    }

    @Operation(summary = "연령대별 기사 조회 API", description = "연령대별 많이 보는 기사 리스트를 조회한다.")
    @GetMapping("/age")
    public ResponseEntity<EntityModel<Result<SlicedResponse<List<GetArticleResponse>>>>> getArticlesByBirthYear(
            @NullableUserDetails CustomUserDetails userDetails,
            @RequestParam(required = false, defaultValue = "0", value = "pageNumber") int pageNumber,
            @RequestParam(required = false, defaultValue = "ALL", value = "filter") String filter
    ) {
       return ResponseUtil.ok(
               Result.of(articleService.getArticlesByBirthYear(userDetails, pageNumber, filter))
       );
    }

}
