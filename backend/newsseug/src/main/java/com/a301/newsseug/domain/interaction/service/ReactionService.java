package com.a301.newsseug.domain.interaction.service;

import com.a301.newsseug.domain.article.model.entity.Article;
import com.a301.newsseug.domain.interaction.model.entity.type.ReactionType;
import com.a301.newsseug.domain.interaction.repository.ReactionRepository;
import com.a301.newsseug.domain.member.model.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReactionService {

    private final ReactionRepository reactionRepository;

    public boolean hasReacted(Member member, Article article, ReactionType type) {
        return reactionRepository.existsByMemberAndArticleAndType(member, article, type);
    }

}
