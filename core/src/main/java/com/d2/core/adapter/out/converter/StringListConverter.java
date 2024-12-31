package com.d2.core.adapter.out.converter;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

public class StringListConverter extends JsonArrayConverter<String> {

	public StringListConverter() {
		super(new TypeReference<List<String>>() {
		});
	}
}
