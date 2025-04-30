package com.a301.newsseug.domain.interaction.model.entity.type;

import com.a301.newsseug.domain.article.exception.InvalidReportTypeException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReactionType {

    LIKE("like"), HATE("hate");

    private final String value;

    public static ReactionType from(String value) {
        try{
            return ReactionType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidReportTypeException();
        }
    }

}
