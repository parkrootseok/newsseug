package com.a301.newsseug.domain.member.factory;

import com.a301.newsseug.domain.member.builder.SubscribeTestBuilder;
import com.a301.newsseug.domain.member.model.entity.Member;
import com.a301.newsseug.domain.member.model.entity.Subscribe;
import com.a301.newsseug.domain.press.model.entity.Press;

public final class SubscribeFactory {

    private SubscribeFactory() {}

    public static Subscribe subscribe(Member member, Press press) {
        return SubscribeTestBuilder.aSubscribe()
                .member(member)
                .press(press)
                .build();
    }

    public static Subscribe subscribe(Long id, Member member, Press press) {
        return SubscribeTestBuilder.aSubscribe()
                .id(id)
                .member(member)
                .press(press)
                .build();
    }

}
