package com.a301.newsseug.domain.article.repository;

import static com.a301.newsseug.domain.article.model.entity.QArticle.article;

import com.a301.newsseug.domain.article.model.dto.ArticleRetrieveCondition;
import com.a301.newsseug.domain.article.model.dto.response.GetArticleSummaryResponseDto;
import com.a301.newsseug.domain.article.model.dto.response.QGetArticleSummaryResponseDto;
import com.a301.newsseug.domain.article.model.entity.Article;
import com.a301.newsseug.domain.article.model.entity.type.CategoryType;
import com.a301.newsseug.domain.article.model.entity.type.ConversionStatus;
import com.a301.newsseug.domain.press.model.entity.Press;
import com.a301.newsseug.global.model.entity.ActivationStatus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
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
    public Slice<GetArticleSummaryResponseDto> findAllArticlesByCondition(
            ArticleRetrieveCondition conditions, Pageable pageable
    ) {
        List<GetArticleSummaryResponseDto> articles =
                createPagingQuery(pageable).where(conditions.toPredicate()).fetch();
        return toSlice(articles, pageable);
    }

    @Override
    public Slice<GetArticleSummaryResponseDto> findAllArticlesByPressAndCondition(ArticleRetrieveCondition conditions, Pageable pageable) {
        List<GetArticleSummaryResponseDto> articles =
                createPagingQuery(pageable).where(conditions.toPredicate()).fetch();
        return toSlice(articles, pageable);
    }

    @Override
    public Slice<GetArticleSummaryResponseDto> findAllArticlesBySubscribedPress(List<Press> presses, ArticleRetrieveCondition conditions, Pageable pageable) {
        List<GetArticleSummaryResponseDto> articles =
                createPagingQuery(pageable)
                        .where(conditions.toPredicate().and(article.press.in(presses)))
                        .fetch();
        return toSlice(articles, pageable);
    }

    @Override
    public Slice<GetArticleSummaryResponseDto> findAllArticlesByTitle(String keyword, Pageable pageable) {
        List<GetArticleSummaryResponseDto> articles =
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

    /**
     * 공통 조건을 처리하는 메서드
     * @param category 카테고리 조건
     * @return BooleanBuilder에 상태와 변환 상태 조건을 추가한 빌더
     */
    private BooleanBuilder createBaseCondition(String category) {

        BooleanBuilder builder = new BooleanBuilder();

        if (Objects.nonNull(category) && !category.equalsIgnoreCase("ALL")) {
            addCategoryCondition(builder, category, article.category::eq);
        }

        builder.and(article.activationStatus.eq(ActivationStatus.ACTIVE));
        builder.and(article.conversionStatus.eq(ConversionStatus.SUCCESS));

        return builder;

    }

    private BooleanBuilder createBaseCondition() {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(article.activationStatus.eq(ActivationStatus.ACTIVE));
        builder.and(article.conversionStatus.eq(ConversionStatus.SUCCESS));
        return builder;
    }

    private <T> void addCategoryCondition(BooleanBuilder builder, String value, Function<CategoryType , BooleanExpression> condition) {
        builder.and(condition.apply(CategoryType.from(value)));
    }

    /**
     * 쿼리를 실행하고 Slice로 반환하는 메서드
     * @param conditions BooleanBuilder에 추가된 조건
     * @param pageable 페이징 정보
     * @return Slice<Article>
     */
    private Slice<Article> createPagingQuery(BooleanBuilder conditions, Pageable pageable) {
        List<Article> content = jpaQueryFactory
                .selectFrom(article)
                .leftJoin(article.press).fetchJoin()
                .where(createBaseCondition())
                .where(conditions)
                .orderBy(new OrderSpecifier<>(Order.DESC, article.sourceCreatedAt))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = content.size() > pageable.getPageSize();

        if (hasNext) {
            content.remove(content.size() - 1);
        }

        return new SliceImpl<>(content, pageable, hasNext);

    }

    private JPAQuery<GetArticleSummaryResponseDto> createPagingQuery(Pageable pageable) {
        return jpaQueryFactory
                .select(new QGetArticleSummaryResponseDto(
                        article.id,
                        article.pressName,
                        article.thumbnailUrl,
                        article.title,
                        article.viewCount,
                        article.sourceCreatedAt
                ))
                .from(article)
                .orderBy(article.sourceCreatedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1);
    }

    private Slice<GetArticleSummaryResponseDto> toSlice(List<GetArticleSummaryResponseDto> content, Pageable pageable) {
        boolean hasNext = content.size() > pageable.getPageSize();
        if (hasNext) {
            content.remove(content.size() - 1);
        }
        return new SliceImpl<>(content, pageable, hasNext);
    }

}
