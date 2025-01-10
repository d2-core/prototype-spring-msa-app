package com.d2.productservice.adapter.out.persistence.lecture;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiExceptionImpl;
import com.d2.core.model.enums.ReferenceType;
import com.d2.productservice.adapter.out.persistence.video.QVideoSteamJpaEntity;
import com.d2.productservice.adapter.out.persistence.video.VideoSteamJpaEntity;
import com.d2.productservice.application.port.out.LecturePort;
import com.d2.productservice.model.dto.DeleteFileDto;
import com.d2.productservice.model.dto.LectureDto;
import com.d2.productservice.model.dto.LectureReferenceDto;
import com.d2.productservice.model.dto.LectureTimelineDto;
import com.d2.productservice.model.dto.VideoStreamDto;
import com.d2.productservice.model.enums.FileType;
import com.d2.productservice.model.enums.LectureExportType;
import com.d2.productservice.model.enums.LectureStatus;
import com.d2.productservice.model.enums.LectureType;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class LecturePersistenceAdapter implements LecturePort {

	private final LectureJpaRepository lectureJpaRepository;
	private final JPQLQueryFactory jpqlQueryFactory;

	@Override
	public LectureDto register(Long courseId, LectureType lectureType, String title, String thumbnailImageUrl,
		String description, List<LectureTimelineDto> chapters, List<LectureReferenceDto> lectureReferences,
		LectureExportType lectureExportType) {

		Long orderIndex = lectureCount(courseId) + 1;

		LectureJpaEntity lectureJpaEntity = lectureJpaRepository.save(
			new LectureJpaEntity(courseId, lectureType, title, description, thumbnailImageUrl, chapters,
				lectureReferences, lectureExportType, orderIndex, LectureStatus.REGISTER));

		return LectureDto.from(lectureJpaEntity);
	}

	@Override
	public LectureDto update(Long lectureId, Long courseId, LectureType lectureType, String title,
		String thumbnailImageUrl, String description, List<LectureTimelineDto> chapters,
		List<LectureReferenceDto> lectureReferences, LectureExportType lectureExportType) {

		LectureJpaEntity lectureJpaEntity = getLectureJpaEntity(lectureId);

		/*
		Update -> CourseEntity 영속성 유지 안되지만 Id만 필요 (그럼으로 상관 없음)
		 */
		LectureJpaEntity updateLectureEntity = lectureJpaEntity.update(courseId, lectureType, title, description,
			thumbnailImageUrl, chapters, lectureReferences, lectureExportType, lectureJpaEntity.getOrderIndex(),
			lectureJpaEntity.getLectureStatus());

		LectureJpaEntity savedEntity = lectureJpaRepository.save(updateLectureEntity);

		return LectureDto.from(savedEntity);
	}

	@Override
	public LectureDto update(Long lectureId, String thumbnailImageUrl, List<LectureReferenceDto> lectureReferences) {
		LectureJpaEntity lectureJpaEntity = getLectureJpaEntity(lectureId);

		LectureJpaEntity savedEntity = lectureJpaRepository.save(
			lectureJpaEntity.update(thumbnailImageUrl, lectureReferences));

		return LectureDto.from(savedEntity);
	}

	@Override
	public LectureDto update(Long lectureId, LectureExportType lectureExportType) {
		LectureJpaEntity lectureJpaEntity = getLectureJpaEntity(lectureId);

		LectureJpaEntity savedEntity = lectureJpaRepository.save(lectureJpaEntity.update(lectureExportType));

		return LectureDto.from(savedEntity);
	}

	@Override
	public LectureDto update(Long lectureId, Long videoStreamId) {
		LectureJpaEntity lectureJpaEntity = getLectureJpaEntity(lectureId);

		LectureJpaEntity savedEntity = lectureJpaRepository.save(lectureJpaEntity.update(videoStreamId));

		return LectureDto.from(savedEntity);
	}

	@Override
	public void delete(Long lectureId) {
		lectureJpaRepository.deleteById(lectureId);
	}

	@Override
	public List<LectureDto> getLectureList() {
		return lectureJpaRepository.findAll()
			.stream().map(LectureDto::from)
			.collect(Collectors.toList());
	}

	@Override
	public List<LectureDto> getLectureList(Long courseId) {
		QLectureJpaEntity lectureJpaEntity = QLectureJpaEntity.lectureJpaEntity;
		return jpqlQueryFactory.select(Projections.fields(LectureDto.class,
				lectureJpaEntity.id,
				Expressions.asNumber(courseId).as("courseId"),
				lectureJpaEntity.title,
				lectureJpaEntity.thumbnailImageUrl,
				lectureJpaEntity.lectureType,
				lectureJpaEntity.orderIndex.as("order"),
				lectureJpaEntity.lectureExportType,
				lectureJpaEntity.chapters,
				lectureJpaEntity.lectureReferences,
				lectureJpaEntity.lectureStatus,
				lectureJpaEntity.createdAt,
				lectureJpaEntity.updatedAt
			))
			.from(lectureJpaEntity)
			.where(lectureJpaEntity.courseJpaEntity.id.eq(courseId))
			.fetch();
	}

	@Override
	public List<VideoStreamDto> getLectureVideoConditionList(List<Long> lectureIds) {
		QVideoSteamJpaEntity videoStream = QVideoSteamJpaEntity.videoSteamJpaEntity;
		QLectureJpaEntity lecture = QLectureJpaEntity.lectureJpaEntity;

		List<VideoStreamDto> videoStreams = jpqlQueryFactory
			.select(Projections.fields(VideoStreamDto.class,
				videoStream.id,
				lecture.id.as("foreignId"),
				videoStream.videoUrl,
				videoStream.duration,
				videoStream.videoTranscodeStatus,
				videoStream.transcodeProgress
			))
			.from(lecture)
			.join(videoStream)
			.on(lecture.videoSteamJpaEntity.id.eq(videoStream.id))
			.where(lecture.id.in(lectureIds))
			.fetch();

		if (videoStreams.isEmpty()) {
			throw new ApiExceptionImpl(ErrorCodeImpl.NOT_FOUND);
		}

		return videoStreams;
	}

	@Override
	public LectureDto getLecture(Long lectureId) {
		QLectureJpaEntity lectureJpaEntity = QLectureJpaEntity.lectureJpaEntity;
		QVideoSteamJpaEntity videoSteamJpaEntity = QVideoSteamJpaEntity.videoSteamJpaEntity;

		LectureDto result = jpqlQueryFactory
			.select(Projections.fields(LectureDto.class,
				Expressions.asNumber(lectureId).as("id"),
				lectureJpaEntity.courseJpaEntity.id.as("courseId"),
				Projections.fields(VideoStreamDto.class,
					videoSteamJpaEntity.id,
					videoSteamJpaEntity.videoUrl,
					videoSteamJpaEntity.duration,
					videoSteamJpaEntity.supportedVideoResolutions
				).as("videoStreamDto"),
				lectureJpaEntity.title,
				lectureJpaEntity.description,
				lectureJpaEntity.thumbnailImageUrl,
				lectureJpaEntity.lectureType,
				lectureJpaEntity.orderIndex.as("order"),
				lectureJpaEntity.lectureExportType,
				lectureJpaEntity.chapters,
				lectureJpaEntity.lectureReferences,
				lectureJpaEntity.lectureStatus,
				lectureJpaEntity.createdAt,
				lectureJpaEntity.updatedAt
			))
			.from(lectureJpaEntity)
			.leftJoin(lectureJpaEntity.videoSteamJpaEntity, videoSteamJpaEntity)
			.where(lectureJpaEntity.id.eq(lectureId))
			.fetchOne();

		List<VideoSteamJpaEntity> videoSteamJpaEntities = jpqlQueryFactory.selectFrom(videoSteamJpaEntity)
			.fetch();

		if (result != null) {
			return result;
		}
		throw new ApiExceptionImpl(ErrorCodeImpl.NOT_FOUND, "id: %s".formatted(lectureId));
	}

	@Override
	public List<DeleteFileDto> getFileUrls(Long lectureId) {
		QLectureJpaEntity lectureJpaEntity = QLectureJpaEntity.lectureJpaEntity;
		QVideoSteamJpaEntity videoSteamJpaEntity = QVideoSteamJpaEntity.videoSteamJpaEntity;
		Tuple result = jpqlQueryFactory.select(lectureJpaEntity.thumbnailImageUrl, lectureJpaEntity.lectureReferences,
				videoSteamJpaEntity.videoUrl)
			.from(lectureJpaEntity)
			.leftJoin(videoSteamJpaEntity)
			.on(lectureJpaEntity.videoSteamJpaEntity.id.eq(videoSteamJpaEntity.id))
			.where(lectureJpaEntity.id.eq(lectureId))
			.fetchFirst();

		if (result != null) {
			List<DeleteFileDto> allUrls = new ArrayList<>();
			String thumbnailUrl = result.get(lectureJpaEntity.thumbnailImageUrl);
			if (thumbnailUrl != null) {
				allUrls.add(new DeleteFileDto(FileType.IMAGE, thumbnailUrl));
			}

			List<LectureReferenceDto> lectureReferences = result.get(lectureJpaEntity.lectureReferences);
			if (lectureReferences != null) {
				List<DeleteFileDto> lectureReferenceUrls = lectureReferences
					.stream()
					.filter(lectureReferenceDto -> lectureReferenceDto.getReferenceType().equals(ReferenceType.PDF))
					.map(lectureReferenceDto -> new DeleteFileDto(FileType.PDF, lectureReferenceDto.getUrl()))
					.toList();
				allUrls.addAll(lectureReferenceUrls);
			}

			String videoUrl = result.get(videoSteamJpaEntity.videoUrl);
			if (videoUrl != null) {
				allUrls.add(new DeleteFileDto(FileType.VIDEO, videoUrl));
			}

			return allUrls;
		}
		throw new ApiExceptionImpl(ErrorCodeImpl.NOT_FOUND, "id: %s".formatted(lectureId));
	}

	private LectureJpaEntity getLectureJpaEntity(Long lectureId) {
		return lectureJpaRepository.findById(lectureId)
			.orElseThrow(() -> new ApiExceptionImpl(ErrorCodeImpl.NOT_FOUND, "id: %s".formatted(lectureId)));
	}

	private Boolean existLecture(Long lectureId) {
		QLectureJpaEntity lectureJpaEntity = QLectureJpaEntity.lectureJpaEntity;
		Integer result = jpqlQueryFactory.selectOne()
			.from(lectureJpaEntity)
			.where(lectureJpaEntity.id.eq(lectureId))
			.fetchFirst();

		return result != null;
	}

	private Long lectureCount(Long courseId) {
		QLectureJpaEntity lectureJpaEntity = QLectureJpaEntity.lectureJpaEntity;
		return jpqlQueryFactory.select(lectureJpaEntity.count())
			.from(lectureJpaEntity)
			.where(lectureJpaEntity.courseJpaEntity.id.eq(courseId))
			.fetchFirst();
	}
}
