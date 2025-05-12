package com.a301.newsseug.domain.interaction.usecase;

import com.a301.newsseug.domain.article.model.entity.Article;
import com.a301.newsseug.domain.article.service.ArticleQueryService;
import com.a301.newsseug.domain.auth.model.entity.CustomUserDetails;
import com.a301.newsseug.domain.interaction.model.entity.type.ReactionType;
import com.a301.newsseug.domain.interaction.service.ReactionCRUDService;
import com.a301.newsseug.domain.member.model.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReactionUseCase {
    
    private final ArticleQueryService articleQueryService;
    private final ReactionCRUDService reactionCRUDService;

    public boolean doReactionToArticle(CustomUserDetails userDetails, Long articleId, String type) {
        Member loginMember = userDetails.getMember();
        Article article = articleQueryService.getArticle(articleId);
        reactionCRUDService.createReaction(loginMember, article, ReactionType.from(type));
        return Boolean.TRUE;
    }

    public boolean cancelReactionToArticle(CustomUserDetails userDetails, Long articleId, String type) {
        Member loginMember = userDetails.getMember();
        Article article = articleQueryService.getArticle(articleId);
        reactionCRUDService.deleteReaction(loginMember, article, ReactionType.from(type));
        return Boolean.TRUE;
    }

}
