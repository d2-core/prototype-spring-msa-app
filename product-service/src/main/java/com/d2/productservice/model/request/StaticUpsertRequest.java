package com.d2.productservice.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StaticUpsertRequest {
	@NotEmpty
	private String name;

	@NotEmpty
	private String description;
}
