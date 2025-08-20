package com.a301.newsseug.domain.press.builder;

import com.a301.newsseug.domain.press.model.entity.Press;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.test.util.ReflectionTestUtils;

public class PressTestBuilder {

    private static final AtomicLong SEQ = new AtomicLong(1L);

    private Long id = SEQ.getAndIncrement();
    private String name = "press";
    private String imageUrl = "imageUrl";
    private String description = "description";
    private Long subscribeCount = 0L;

    private PressTestBuilder() { }

    public static PressTestBuilder aPress() {
        return new PressTestBuilder();
    }

    public PressTestBuilder id(Long id) {
        this.id = id;
        return this;
    }
    public PressTestBuilder name(String name) {
        this.name = name;
        return this;
    }
    public PressTestBuilder imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }
    public PressTestBuilder description(String description) {
        this.description = description;
        return this;
    }
    public PressTestBuilder subscribeCount(Long subscribeCount) {
        this.subscribeCount = subscribeCount;
        return this;
    }

    public Press build() {
        Press press = Press.builder()
                .name(this.name)
                .imageUrl(this.imageUrl)
                .description(this.description)
                .build();

        ReflectionTestUtils.setField(press, "id", this.id);
        return press;
    }

}
