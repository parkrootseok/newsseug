package com.a301.newsseug.domain.article.repository;

import static com.a301.newsseug.domain.article.model.entity.QArticle.article;

import com.a301.newsseug.domain.article.model.dto.ArticleRetrieveCondition;
import com.a301.newsseug.domain.article.model.dto.ArticleSummaryDto;
import com.a301.newsseug.domain.article.model.dto.QArticleSummaryDto;
import com.a301.newsseug.domain.press.model.entity.Press;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class ArticleCustomRepositoryImpl implements ArticleCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<ArticleSummaryDto> findAllArticlesByCondition(
            ArticleRetrieveCondition conditions, Pageable pageable
    ) {
        List<ArticleSummaryDto> articles =
                createPagingQuery(pageable).where(conditions.toPredicate()).fetch();
        return toSlice(articles, pageable);
    }

    @Override
    public Slice<ArticleSummaryDto> findAllArticlesByPressAndCondition(ArticleRetrieveCondition conditions, Pageable pageable) {
        List<ArticleSummaryDto> articles =
                createPagingQuery(pageable).where(conditions.toPredicate()).fetch();
        return toSlice(articles, pageable);
    }

    @Override
    public Slice<ArticleSummaryDto> findAllArticlesBySubscribedPress(List<Press> presses, ArticleRetrieveCondition conditions, Pageable pageable) {
        List<ArticleSummaryDto> articles =
                createPagingQuery(pageable)
                        .where(conditions.toPredicate().and(article.press.in(presses)))
                        .fetch();
        return toSlice(articles, pageable);
    }

    @Override
    public Slice<ArticleSummaryDto> findAllArticlesByTitle(String keyword, Pageable pageable) {
        List<ArticleSummaryDto> articles =
                createPagingQuery(pageable)
                        .where(article.title.containsIgnoreCase(keyword))
                        .fetch();
        return toSlice(articles, pageable);
    }

    @Override
    @Modifying
    @Transactional
    public void batchUpdateCount(String field, Map<String, Long> countingLog) {
        PathBuilder<Long> fieldPath = new PathBuilder<>(Long.class, "article." + field);
        CaseBuilder caseBuilder = new CaseBuilder();
        CaseBuilder.Cases<Long, NumberExpression<Long>> caseExpression = null;

        for (Map.Entry<String, Long> entry : countingLog.entrySet()) {
            Long id = Long.parseLong(entry.getKey());
            Long count = entry.getValue();
            if (caseExpression == null) {
                caseExpression = caseBuilder.when(article.id.eq(id)).then(count);
            } else {
                caseExpression = caseExpression.when(article.id.eq(id)).then(count);
            }
        }
        NumberExpression<Long> finalExpression = caseExpression.otherwise(0L);

        jpaQueryFactory.update(article)
                .set(fieldPath, Expressions.numberTemplate(Long.class, "{0} + {1}", fieldPath, finalExpression))
                .where(article.id.in(countingLog.keySet().stream().map(Long::parseLong).toList()))
                .execute();
    }

    private JPAQuery<ArticleSummaryDto> createPagingQuery(Pageable pageable) {
        return jpaQueryFactory
                .select(new QArticleSummaryDto(
                        article.id,
                        article.thumbnailUrl,
                        article.title,
                        article.viewCount,
                        article.sourceCreatedAt,
                        article.press.id
                ))
                .from(article)
                .orderBy(article.sourceCreatedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1);
    }

    private Slice<ArticleSummaryDto> toSlice(List<ArticleSummaryDto> content, Pageable pageable) {
        boolean hasNext = content.size() > pageable.getPageSize();
        if (hasNext) {
            content.remove(content.size() - 1);
        }
        return new SliceImpl<>(content, pageable, hasNext);
    }

}
