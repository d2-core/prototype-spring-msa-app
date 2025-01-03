package com.d2.productservice.model.request;

import java.util.List;

import com.d2.core.model.dto.FileForm;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CourseUpsertRequest {
	@NotNull
	private Long teacherId;

	@NotEmpty
	private List<FileForm> thumbnailImageFiles;

	@NotNull
	private Long courseCategoryId;

	@NotEmpty
	@Size(min = 5)
	private String title;

	@NotEmpty
	@Size(min = 10)
	private String subTitle;

	@NotEmpty
	@Size(min = 10)
	private String descriptionWithMarkdown;

	@NotNull
	private Long courseLevelId;

	private List<String> tags;

	private Integer price;
}
