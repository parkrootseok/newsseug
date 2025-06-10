package com.a301.newsseug.domain.reaction.model.entity;

import com.a301.newsseug.domain.article.model.entity.Article;
import com.a301.newsseug.domain.reaction.model.entity.type.ReactionType;
import com.a301.newsseug.domain.member.model.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "reactions",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_member_article_type",
                        columnNames = {"member_id", "article_id", "type"}
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @Enumerated(EnumType.STRING)
    private ReactionType type;

    @Builder
    public Reaction(Member member, Article article, ReactionType type) {
        this.member = member;
        this.article = article;
        this.type = type;
    }

}
