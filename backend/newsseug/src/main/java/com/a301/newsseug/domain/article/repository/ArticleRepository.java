package com.a301.newsseug.domain.article.repository;

import com.a301.newsseug.domain.article.exception.NotExistArticleException;
import com.a301.newsseug.domain.article.model.entity.Article;
import com.a301.newsseug.domain.article.model.entity.type.CategoryType;
import com.a301.newsseug.domain.article.model.entity.type.ConversionStatus;
import com.a301.newsseug.global.model.entity.ActivationStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleCustomRepository {

    default Article getOrThrow(Long id) {
        return findByArticleIdAndConversionStatus(id, ConversionStatus.SUCCESS)
                .orElseThrow(NotExistArticleException::new);
    }

    @Query(value = "SELECT a "
            + "FROM Article a "
            + "LEFT JOIN FETCH a.press "
            + "WHERE a.category = :category "
            + "AND a.activationStatus = :activationStatus "
            + "AND a.conversionStatus = :conversionStatus "
            + "ORDER BY RAND()")
    Slice<Article> findAllByCategoryOrderByRandom(
            @Param("category") CategoryType categoryType,
            @Param("activationStatus") ActivationStatus activationStatus,
            @Param("conversionStatus") ConversionStatus conversionStatus,
            Pageable pageable
    );

    @Query(value = "SELECT a "
            + "FROM Article a "
            + "LEFT JOIN FETCH a.press "
            + "WHERE a.activationStatus = :activationStatus "
            + "AND a.conversionStatus = :conversionStatus "
            + "ORDER BY RAND()")
    Slice<Article> findAllOrderByRandom(
            @Param("activationStatus") ActivationStatus activationStatus,
            @Param("conversionStatus") ConversionStatus conversionStatus,
            Pageable pageable
    );

    Optional<Article> findByArticleIdAndConversionStatus(Long id, ConversionStatus conversionStatus);

}
