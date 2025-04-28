package com.a301.newsseug.domain.article.usecase;

import com.a301.newsseug.domain.article.model.dto.response.GetArticleResponse;
import com.a301.newsseug.domain.article.model.entity.type.CategoryType;
import com.a301.newsseug.domain.article.service.ArticleQueryService;
import com.a301.newsseug.global.model.dto.SlicedResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleUseCase {

    private final ArticleQueryService queryService;

    /**
     * [전체 기사 조회]
     * 필터를 선택한 경우에는 해당 카테고리 기사만, 아닌 경우엔 구분 없이 모든 기사를 조회
     *
     * @param filter        카테고리 (없으면 전체)
     * @param pageNumber    페이지 번호
     */
    public SlicedResponse<List<GetArticleResponse>> retrieveAllArticle(String filter, int pageNumber) {
        CategoryType category = CategoryType.from(filter);
        if (category.equals(CategoryType.ALL)) {
            // 카테고리를 선택하지 않은 경우
            return queryService.getArticles(pageNumber);
        } else {
            // 특정 카테고리를 선택한 경우
            return queryService.getArticlesByCategory(category, pageNumber);
        }
    }
    
}
