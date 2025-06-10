package com.a301.newsseug.external.caffeine;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CacheTypes {

    ARTICLE("article", 1024, 300),
    PRESS("press", 1024, 0);

    private final String name;
    private final int maximumSize;
    private final int expireAfterWrite;

}
