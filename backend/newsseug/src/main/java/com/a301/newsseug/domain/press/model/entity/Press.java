package com.a301.newsseug.domain.press.model.entity;

import jakarta.persistence.*;
import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Press press)) {
            return false;
        }
        return Objects.equals(id, press.id) && Objects.equals(name, press.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}
