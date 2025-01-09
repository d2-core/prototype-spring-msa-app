package com.d2.productservice.model.request;

import java.util.ArrayList;
import java.util.List;

import com.d2.core.model.dto.FileFormDto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CourseUpsertFileRequest {
	@NotEmpty
	private List<FileFormDto> thumbnailImageFiles;

	public CourseUpsertFileRequest() {
		thumbnailImageFiles = new ArrayList<>();
	}
}
