package com.d2.core.adapter.out.converter;

import java.util.List;

import org.springframework.util.StringUtils;

import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiExceptionImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class JsonArrayConverter<T> implements AttributeConverter<List<T>, String> {

	private final ObjectMapper objectMapper = new ObjectMapper();
	private final TypeReference<List<T>> typeReference;

	public JsonArrayConverter(TypeReference<List<T>> typeReference) {
		this.typeReference = typeReference;
	}

	@Override
	public String convertToDatabaseColumn(List<T> attribute) {
		if (attribute == null || attribute.isEmpty()) {
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
	public List<T> convertToEntityAttribute(String dbData) {
		if (StringUtils.hasText(dbData)) {
			try {
				return objectMapper.readValue(dbData, typeReference);
			} catch (Exception e) {
				throw new ApiExceptionImpl(ErrorCodeImpl.INTERNAL_SERVER_ERROR, e, "data: %s".formatted(dbData));
			}
		}
		return null;
	}
}
