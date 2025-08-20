package com.a301.newsseug.domain.press.factory;

import com.a301.newsseug.domain.press.builder.PressTestBuilder;
import com.a301.newsseug.domain.press.model.entity.Press;

public final class PressFactory {

	private PressFactory() { }

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

}
