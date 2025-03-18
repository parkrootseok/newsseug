package com.a301.newsseug.domain.interaction.service;

import com.a301.newsseug.domain.article.model.entity.Article;
import com.a301.newsseug.domain.article.repository.ArticleRepository;
import com.a301.newsseug.domain.auth.model.entity.CustomUserDetails;
import com.a301.newsseug.domain.interaction.model.entity.Hate;
import com.a301.newsseug.domain.interaction.model.entity.Like;
import com.a301.newsseug.domain.interaction.repository.HateRepository;
import com.a301.newsseug.domain.interaction.repository.LikeRepository;
import com.a301.newsseug.domain.member.model.entity.Member;
import com.a301.newsseug.domain.counting.event.CountingEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final ApplicationEventPublisher eventPublisher;
    private final ArticleRepository articleRepository;
    private final LikeRepository likeRepository;
    private final HateRepository hateRepository;

    @Override
    @Transactional
    public void createLike(CustomUserDetails userDetails, Long articleId) {

        Member loginMember = userDetails.getMember();
        Article article = articleRepository.getOrThrow(articleId);
        Optional<Hate> hate = hateRepository.findByMemberAndArticle(loginMember, article);
        if(hate.isPresent()) {
            hateRepository.delete(hate.get());
            eventPublisher.publishEvent(
                    CountingEvent.builder()
                            .hash("article:hateCount:")
                            .id(articleId)
                            .delta(-1L)
                            .build()
            );
        }

        likeRepository.save(Like.builder()
                .member(loginMember)
                .article(article)
                .build()
        );

        eventPublisher.publishEvent(
                CountingEvent.builder()
                        .hash("article:likeCount:")
                        .id(articleId)
                        .delta(1L)
                        .build()
        );

    }

    @Override
    @Transactional
    public void deleteLike(CustomUserDetails userDetails, Long articleId) {

        Member loginMember = userDetails.getMember();
        Article article = articleRepository.getOrThrow(articleId);
        Like like = likeRepository.getOrThrow(loginMember, article);
        likeRepository.delete(like);
        eventPublisher.publishEvent(
                CountingEvent.builder()
                        .hash("article:likeCount:")
                        .id(articleId)
                        .delta(-1L)
                        .build()
        );

    }

}
