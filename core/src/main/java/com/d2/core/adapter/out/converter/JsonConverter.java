package com.d2.core.adapter.out.converter;

import org.springframework.util.StringUtils;

import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiExceptionImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class JsonConverter<T> implements AttributeConverter<T, String> {
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final Class<T> type;

	public JsonConverter(Class<T> type) {
		this.type = type;
	}

	@Override
	public String convertToDatabaseColumn(T attribute) {
		if (attribute == null) {
			return null;
		}
		try {
			return objectMapper.writeValueAsString(attribute);
		} catch (Exception e) {
			throw new ApiExceptionImpl(ErrorCodeImpl.INTERNAL_SERVER_ERROR, e,
				"attr: %s".formatted(attribute.toString()));
		}
	}

	@Override
	public T convertToEntityAttribute(String dbData) {
		if (StringUtils.hasText(dbData)) {
			try {
				return objectMapper.readValue(dbData, type);
			} catch (Exception e) {
				throw new ApiExceptionImpl(ErrorCodeImpl.INTERNAL_SERVER_ERROR, e, "data: %s".formatted(dbData));
			}
		}
		return null;
	}
}
