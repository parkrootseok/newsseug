package com.a301.newsseug.external.oauth.model.entity.type;

import com.a301.newsseug.domain.member.exception.InvalidProviderTypeException;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProviderType {

    KAKAO("kakao"), GOOGLE("google");

    private final String value;

    public static ProviderType from(String value) {
        return Arrays.stream(values())
                .filter(p -> p.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(InvalidProviderTypeException::new);
    }

}
