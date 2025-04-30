package com.a301.newsseug.domain.interaction.service;

import com.a301.newsseug.domain.article.model.entity.Article;
import com.a301.newsseug.domain.counting.event.ReactionCountingEvent;
import com.a301.newsseug.domain.interaction.model.entity.Reaction;
import com.a301.newsseug.domain.interaction.model.entity.type.ReactionType;
import com.a301.newsseug.domain.interaction.repository.ReactionRepository;
import com.a301.newsseug.domain.member.model.entity.Member;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReactionCRUDService {

    private final ApplicationEventPublisher eventPublisher;
    private final ReactionRepository reactionRepository;

    @Transactional
    public void createReaction(Member member, Article article, ReactionType type) {

        Optional<Reaction> alreadyExistedReaction = reactionRepository.findByMemberAndArticle(member, article);

        if (alreadyExistedReaction.isPresent()) {

            Reaction oldReaction = alreadyExistedReaction.get();
            if (oldReaction.getType().equals(type)) {
                return;
            }

            reactionRepository.delete(oldReaction);
            eventPublisher.publishEvent(
                    ReactionCountingEvent.of(oldReaction.getType(), article.getId(), -1L)
            );

        }

        reactionRepository.save(Reaction.builder()
                .member(member)
                .article(article)
                .type(type)
                .build()
        );
        eventPublisher.publishEvent(ReactionCountingEvent.of(type, article.getId(), 1L));

    }

    @Transactional
    public void deleteReaction(Member member, Article article, ReactionType type) {
        Reaction reaction = reactionRepository.findOrThrow(member, article, type);
        reactionRepository.delete(reaction);
        eventPublisher.publishEvent(ReactionCountingEvent.of(reaction.getType(), article.getId(), -1L));
    }

}
