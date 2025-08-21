package com.a301.newsseug.domain.member.builder;

import com.a301.newsseug.domain.member.model.entity.Member;
import com.a301.newsseug.domain.member.model.entity.Subscribe;
import com.a301.newsseug.domain.press.model.entity.Press;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.atomic.AtomicLong;

public class SubscribeTestBuilder {

    private static final AtomicLong SEQ = new AtomicLong(1L);

    private Long id = SEQ.getAndIncrement();
    private Member member;
    private Press press;

    private SubscribeTestBuilder() {}

    public static SubscribeTestBuilder aSubscribe() {
        return new SubscribeTestBuilder();
    }

    public SubscribeTestBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public SubscribeTestBuilder member(Member member) {
        this.member = member;
        return this;
    }

    public SubscribeTestBuilder press(Press press) {
        this.press = press;
        return this;
    }

    public Subscribe build() {
        Subscribe subscribe = Subscribe.builder()
                .member(this.member)
                .press(this.press)
                .build();
        ReflectionTestUtils.setField(subscribe, "id", this.id);
        return subscribe;
    }

}
