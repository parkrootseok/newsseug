package com.a301.newsseug.domain.member.model.entity.type;

import com.a301.newsseug.domain.member.exception.InvalidGenderTypeException;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GenderType {

    MALE("male"), FEMALE("female");

    private final String value;

    public static GenderType from(String value) {
        return Arrays.stream(values())
                .filter(g -> g.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(InvalidGenderTypeException::new);
    }

}
