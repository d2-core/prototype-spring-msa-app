package com.d2.productservice.model.request;

import java.util.List;

import com.d2.productservice.model.dto.LectureTimelineDto;
import com.d2.productservice.model.enums.LectureExportType;
import com.d2.productservice.model.enums.LectureType;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LectureUpsertRequest {

	@NotNull
	private Long courseId;

	@NotNull
	private LectureType lectureType;

	@NotEmpty
	@Size(min = 5)
	private String title;

	@NotEmpty
	@Size(min = 10)
	private String description;

	@NotNull
	private List<LectureTimelineDto> chapters;

	@NotNull
	private LectureExportType lectureExportType;
}
