package com.a301.newsseug.domain.reaction.usecase;

import com.a301.newsseug.domain.article.model.entity.Article;
import com.a301.newsseug.domain.article.service.ArticleCacheManager;
import com.a301.newsseug.domain.auth.exception.RequiredLoginException;
import com.a301.newsseug.domain.auth.model.entity.CustomUserDetails;
import com.a301.newsseug.domain.reaction.model.entity.type.ReactionType;
import com.a301.newsseug.domain.reaction.service.ReactionCRUDService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReactionUseCase {

    private final ArticleCacheManager articleCacheManager;
    private final ReactionCRUDService reactionCRUDService;

    public boolean doReactionToArticle(CustomUserDetails userDetails, Long articleId, String type) {
        if (!userDetails.isEnabled()) {
            throw new RequiredLoginException();
        }
        Article article = articleCacheManager.getCachedArticle(articleId);
        return reactionCRUDService.createReaction(userDetails.getMember(), article, ReactionType.from(type));
    }

    public boolean cancelReactionToArticle(CustomUserDetails userDetails, Long articleId, String type) {
        if (!userDetails.isEnabled()) {
            throw new RequiredLoginException();
        }
        Article article = articleCacheManager.getCachedArticle(articleId);
        return reactionCRUDService.deleteReaction(userDetails.getMember(), article, ReactionType.from(type));
    }

}
