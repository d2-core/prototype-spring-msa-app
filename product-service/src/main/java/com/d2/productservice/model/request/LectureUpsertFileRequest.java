package com.d2.productservice.model.request;

import java.util.ArrayList;
import java.util.List;

import com.d2.core.model.dto.FileFormDto;
import com.d2.core.model.dto.ReferenceFileFormDto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LectureUpsertFileRequest {

	@NotNull
	private FileFormDto thumbnailFile;

	private FileFormDto videoFile;

	@NotNull
	private List<ReferenceFileFormDto> lectureReferenceFiles;

	public LectureUpsertFileRequest() {
		this.lectureReferenceFiles = new ArrayList<>();
	}
}
