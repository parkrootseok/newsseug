package com.a301.newsseug.domain.counting.event;

import com.a301.newsseug.domain.reaction.model.entity.type.ReactionType;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record ReactionCountingEvent(String hash, String id, Long delta) {

    private static final String LIKE_HASH_KEY = "article:count:";
    private static final String HATE_HASH_KEY = "article:hateCount:";

    public static ReactionCountingEvent of(ReactionType type, Long id, Long delta) {
        return ReactionCountingEvent.builder()
                .hash(type.equals(ReactionType.LIKE) ? LIKE_HASH_KEY : HATE_HASH_KEY)
                .id(id.toString())
                .delta(delta)
                .build();
    }

}
