package com.a301.newsseug.external.caffeine;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CacheTypes {

    ARTICLE("article");

    static class CacheConfig {
        static final Integer DEFAULT_MAX_SIZE = 1000;
        static final Integer DEFAULT_TTL_SEC = 60;
    }

    private String name;
    private Integer maximumSize;
    private Integer expireAfterWrite;

    CacheTypes(String name) {
        this.name = name;
        this.maximumSize = CacheConfig.DEFAULT_MAX_SIZE;
        this.expireAfterWrite = CacheConfig.DEFAULT_TTL_SEC;
    }

}
