package com.a301.newsseug.domain.article.model.entity.type;

import com.a301.newsseug.domain.article.exception.InvalidCategoryTypeException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "뉴스 카테고리")
public enum CategoryType {

    @Schema(description = "모든 카테고리")
    ALL("all"),

    @Schema(description = "정치")
    POLITICS("politics"),

    @Schema(description = "겅제")
    ECONOMY("economy"),

    @Schema(description = "국제")
    WORLD("world"),

    @Schema(description = "사건")
    ACCIDENT("accident"),

    @Schema(description = "과학")
    SCIENCE("science"),

    @Schema(description = "사회")
    SOCIETY("society"),

    @Schema(description = "스포츠")
    SPORTS("science");

    private final String value;

    public static CategoryType from(String value) {
        try {
            return CategoryType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidCategoryTypeException();
        }
    }

}
