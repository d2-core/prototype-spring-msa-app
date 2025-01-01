package com.d2.insightservice.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum InsightSort {
	TOP_RATE("평점순"),
	POPULAR("인기순"),
	RECOMMEND("추천순");
	
	private final String description;
}
