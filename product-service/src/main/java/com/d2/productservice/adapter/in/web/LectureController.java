package com.d2.productservice.adapter.in.web;

import java.io.File;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.d2.core.api.API;
import com.d2.core.constant.AuthConstant;
import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiExceptionImpl;
import com.d2.core.model.domain.AdminUserAuth;
import com.d2.core.resolver.AdminUserAuthInjection;
import com.d2.core.utils.FileManager;
import com.d2.productservice.application.port.in.LectureUseCase;
import com.d2.productservice.model.domain.Lecture;
import com.d2.productservice.model.domain.LectureVideoCondition;
import com.d2.productservice.model.enums.LectureType;
import com.d2.productservice.model.request.LectureUpdateExportTypeRequest;
import com.d2.productservice.model.request.LectureUpsertFileRequest;
import com.d2.productservice.model.request.LectureUpsertRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/product")
public class LectureController {

	private final LectureUseCase lectureUseCase;
	private final FileManager fileManager;

	@PostMapping(value = "v1/lectures", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public API<Lecture> registerLecture(
		@AdminUserAuthInjection AdminUserAuth adminUserAuth,
		@Valid @ModelAttribute LectureUpsertFileRequest fileRequest,
		@Valid @RequestPart("request") LectureUpsertRequest bodyRequest
	) {
		Lecture lecture;
		if (!adminUserAuth.getAdminUserId().equals(AuthConstant.NOT_EXIST)) {
			if (bodyRequest.getLectureType().equals(LectureType.VIDEO)) {
				File mutipartTempInputFile = null;
				try {
					mutipartTempInputFile = fileManager.writeMutipartFileTempFile("input", ".mp4",
						fileRequest.getVideoFile().getFile());
				} catch (Exception ex) {
					throw new ApiExceptionImpl(ErrorCodeImpl.INTERNAL_SERVER_ERROR, ex);
				}
				lecture = lectureUseCase.registerVideoLecture(
					bodyRequest.getCourseId(),
					bodyRequest.getTitle(),
					fileRequest.getThumbnailFile(),
					mutipartTempInputFile,
					fileRequest.getVideoFile(),
					bodyRequest.getDescription(),
					bodyRequest.getChapters(),
					fileRequest.getLectureReferenceFiles(),
					bodyRequest.getLectureExportType());
			} else {
				lecture = lectureUseCase.registerDocumentLecture(
					bodyRequest.getCourseId(),
					bodyRequest.getTitle(),
					fileRequest.getThumbnailFile(),
					bodyRequest.getDescription(),
					fileRequest.getLectureReferenceFiles(),
					bodyRequest.getLectureExportType());
			}

			return API.OK(lecture);
		}
		throw new ApiExceptionImpl(ErrorCodeImpl.UNAUTHORIZED);
	}

	@PutMapping(value = "v1/lectures/{lectureId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public API<Lecture> modifyLecture(
		@AdminUserAuthInjection AdminUserAuth adminUserAuth,
		@PathVariable Long lectureId,
		@Valid @ModelAttribute LectureUpsertFileRequest fileRequest,
		@Valid @RequestPart("request") LectureUpsertRequest bodyRequest
	) {
		Lecture lecture;
		if (!adminUserAuth.getAdminUserId().equals(AuthConstant.NOT_EXIST)) {
			if (bodyRequest.getLectureType().equals(LectureType.VIDEO)) {
				File mutipartTempInputFile = null;
				if (fileRequest.getVideoFile().getFile() != null) {
					try {
						mutipartTempInputFile = fileManager.writeMutipartFileTempFile("input", ".mp4",
							fileRequest.getVideoFile().getFile());
					} catch (Exception ex) {
						throw new ApiExceptionImpl(ErrorCodeImpl.INTERNAL_SERVER_ERROR, ex);
					}
				}
				lecture = lectureUseCase.updateVideoLecture(
					lectureId,
					bodyRequest.getCourseId(),
					bodyRequest.getTitle(),
					fileRequest.getThumbnailFile(),
					mutipartTempInputFile,
					fileRequest.getVideoFile(),
					bodyRequest.getDescription(),
					bodyRequest.getChapters(),
					fileRequest.getLectureReferenceFiles(),
					bodyRequest.getLectureExportType());
			} else {
				lecture = lectureUseCase.updateDocumentLecture(
					lectureId,
					bodyRequest.getCourseId(),
					bodyRequest.getTitle(),
					fileRequest.getThumbnailFile(),
					bodyRequest.getDescription(),
					fileRequest.getLectureReferenceFiles(),
					bodyRequest.getLectureExportType());
			}

			return API.OK(lecture);
		}
		throw new ApiExceptionImpl(ErrorCodeImpl.UNAUTHORIZED);
	}

	@DeleteMapping("v1/lectures/{lectureId}")
	public API<Object> deleteLecture(
		@AdminUserAuthInjection AdminUserAuth adminUserAuth,
		@PathVariable Long lectureId
	) {
		if (!adminUserAuth.getAdminUserId().equals(AuthConstant.NOT_EXIST)) {
			lectureUseCase.deleteLecture(lectureId);
			return API.NO_CONTENT();
		}
		throw new ApiExceptionImpl(ErrorCodeImpl.UNAUTHORIZED);
	}

	@GetMapping("v1/lectures")
	public API<List<Lecture>> getLectureList(@AdminUserAuthInjection AdminUserAuth adminUserAuth) {
		if (!adminUserAuth.getAdminUserId().equals(AuthConstant.NOT_EXIST)) {
			return API.OK(lectureUseCase.getLectureList());
		}
		throw new ApiExceptionImpl(ErrorCodeImpl.UNAUTHORIZED);
	}

	@GetMapping("v1/lectures/{lectureId}")
	public API<Lecture> getLecture(
		@AdminUserAuthInjection AdminUserAuth adminUserAuth,
		@PathVariable Long lectureId
	) {
		if (!adminUserAuth.getAdminUserId().equals(AuthConstant.NOT_EXIST)) {
			return API.OK(lectureUseCase.getLecture(lectureId));
		}
		throw new ApiExceptionImpl(ErrorCodeImpl.UNAUTHORIZED);
	}

	@GetMapping("v1/lectures/videos/conditions")
	public API<List<LectureVideoCondition>> getLectureVideoConditions(
		@AdminUserAuthInjection AdminUserAuth adminUserAuth,
		@RequestParam("lectureIds") List<Long> lectureIds
	) {
		if (!adminUserAuth.getAdminUserId().equals(AuthConstant.NOT_EXIST)) {
			return API.OK(lectureUseCase.getLectureVideoConditionList(lectureIds));
		}
		throw new ApiExceptionImpl(ErrorCodeImpl.UNAUTHORIZED);
	}

	@PostMapping("v1/lectures/update-export-type")
	public API<Object> updateExportType(
		@AdminUserAuthInjection AdminUserAuth adminUserAuth,
		@Valid @RequestBody LectureUpdateExportTypeRequest request
	) {
		if (!adminUserAuth.getAdminUserId().equals(AuthConstant.NOT_EXIST)) {
			lectureUseCase.updateExportType(request.getLectureId(), request.getLectureExportType());
			return API.NO_CONTENT();
		}
		throw new ApiExceptionImpl(ErrorCodeImpl.UNAUTHORIZED);
	}
}
