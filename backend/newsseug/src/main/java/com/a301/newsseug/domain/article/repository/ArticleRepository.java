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

    default Article findOrThrow(Long id) {
        return findByIdAndActivationStatusAndConversionStatus(id, ActivationStatus.ACTIVE, ConversionStatus.SUCCESS)
                .orElseThrow(NotExistArticleException::new);
    }

    @EntityGraph(attributePaths = {"press"})
    Optional<Article> findByIdAndActivationStatusAndConversionStatus(Long id, ActivationStatus activationStatus, ConversionStatus conversionStatus);

}
