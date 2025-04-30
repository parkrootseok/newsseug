package com.a301.newsseug.domain.press.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "press")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Press {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String imageUrl;

    private String description;

    private Long subscribeCount;

    @Builder
    public Press(
        String name, String imageUrl, String description
    ) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.subscribeCount = 0L;
    }

    public void incrementSubscribeCount() {
        this.subscribeCount++;
    }

    public void decrementSubscribeCount() {
        if (this.subscribeCount == 0) {
            return;
        }
        this.subscribeCount--;
    }

}
