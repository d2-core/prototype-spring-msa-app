package com.d2.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SortDto<T> {
	private T standard;

	private Order orderBy;

	public static enum Order {
		ASC,
		DESC,
	}
}
