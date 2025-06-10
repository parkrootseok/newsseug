package com.a301.newsseug.domain.reaction.repository;

import com.a301.newsseug.domain.article.model.entity.Article;
import com.a301.newsseug.domain.interaction.exception.NotExistReactionException;
import com.a301.newsseug.domain.reaction.model.entity.Reaction;
import com.a301.newsseug.domain.reaction.model.entity.type.ReactionType;
import com.a301.newsseug.domain.member.model.entity.Member;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactionRepository extends CrudRepository<Reaction, Long> {

    default Reaction findOrThrow(Member member, Article article, ReactionType type) {
        return findByMemberAndArticleAndType(member, article, type)
                .orElseThrow(NotExistReactionException::new);
    }

    Optional<Reaction> findByMemberAndArticle(Member member, Article article);
    Optional<Reaction> findByMemberAndArticleAndType(Member member, Article article, ReactionType type);
    Boolean existsByMemberAndArticleAndType(Member member, Article article, ReactionType type);

}
