package com.d2.productservice.model.request;

import com.d2.productservice.model.enums.LectureExportType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LectureUpdateExportTypeRequest {
	private Long lectureId;
	private LectureExportType lectureExportType;
}
