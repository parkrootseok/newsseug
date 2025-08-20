package com.a301.newsseug.domain.press.factory;

import com.a301.newsseug.domain.press.builder.PressTestBuilder;
import com.a301.newsseug.domain.press.model.entity.Press;

public final class PressFactory {

	private PressFactory() {

	}

	public static Press press() {
		return PressTestBuilder.aPress().build();
	}

	public static Press press(Long id) {
		return PressTestBuilder.aPress()
				.id(id)
				.build();
	}

	public static Press press(Long id, String name) {
		return PressTestBuilder.aPress()
				.id(id)
				.name(name)
				.build();
	}

	public static Press pressWithImage(Long id, String name, String imageUrl) {
		return PressTestBuilder.aPress()
				.id(id)
				.name(name)
				.imageUrl(imageUrl)
				.build();
	}

	public static Press pressWithDescription(Long id, String name, String description) {
		return PressTestBuilder.aPress()
				.id(id)
				.name(name)
				.description(description)
				.build();
	}

	public static Press pressFull(Long id, String name, String description, String imageUrl, Long subscribeCount) {
		return PressTestBuilder.aPress()
				.id(id)
				.name(name)
				.description(description)
				.imageUrl(imageUrl)
				.subscribeCount(subscribeCount)
				.build();
	}

	public static Press pressFull(String name, String description, String imageUrl, Long subscribeCount) {
		return PressTestBuilder.aPress()
				.name(name)
				.description(description)
				.imageUrl(imageUrl)
				.subscribeCount(subscribeCount)
				.build();
	}
}
