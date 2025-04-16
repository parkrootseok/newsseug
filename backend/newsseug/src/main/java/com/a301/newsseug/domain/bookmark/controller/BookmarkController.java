package com.a301.newsseug.domain.bookmark.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.a301.newsseug.domain.article.controller.ArticleController;
import com.a301.newsseug.domain.article.model.dto.response.GetArticleDetailsResponse;
import com.a301.newsseug.domain.auth.model.entity.CustomUserDetails;
import com.a301.newsseug.domain.bookmark.model.dto.request.CreateBookmarkRequest;
import com.a301.newsseug.domain.bookmark.service.BookmarkService;
import com.a301.newsseug.domain.folder.controller.FolderController;
import com.a301.newsseug.domain.interaction.controller.HateController;
import com.a301.newsseug.domain.interaction.controller.LikeController;
import com.a301.newsseug.global.model.dto.Result;
import com.a301.newsseug.global.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "북마크 API")
@RestController
@RequestMapping("/api/v1/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @Operation(summary = "기사 저장", description = "사용자가 기사를 폴더에 저장한다.")
    @PostMapping
    public ResponseEntity<EntityModel<Result<Boolean>>> createBookmark(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid CreateBookmarkRequest request
    ) {
        bookmarkService.createBookmark(userDetails, request);

        List<Link> folderLinks = request.folderIds().stream()
                .map(folderId ->
                        linkTo(methodOn(FolderController.class)
                                .getFolder(userDetails, 0, folderId))
                                .withRel("folder-" + folderId)
                ).toList();

        EntityModel<Result<Boolean>> model = EntityModel.of(
                Result.of(Boolean.TRUE)
        );

        folderLinks.forEach(model::add);

        return ResponseUtil.created(model);

    }

}
