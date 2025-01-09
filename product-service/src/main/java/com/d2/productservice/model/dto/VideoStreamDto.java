package com.d2.productservice.model.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.d2.core.model.enums.VideoResolution;
import com.d2.productservice.adapter.out.persistence.video.VideoSteamJpaEntity;
import com.d2.productservice.model.enums.VideoTranscodeStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoStreamDto {
	private Long id;
	private String videoUrl;
	private Integer duration;
	private String videoFormat;
	private Long originalVideoSize;
	private List<VideoResolution> supportedVideoResolution;
	private List<String> originalSupportableVideoQualities;
	private Integer transcodeProgress;
	private VideoTranscodeStatus videoTranscodeStatus;
	private LocalDateTime createdAt;
	private LocalDateTime updateAt;

	public static VideoStreamDto from(VideoSteamJpaEntity entity) {
		return new VideoStreamDto(
			entity.getId(),
			entity.getVideoUrl(),
			entity.getDuration(),
			entity.getVideoFormat(),
			entity.getOriginalVideoSize(),
			entity.getSupportedVideoResolution(),
			entity.getOriginalSupportableVideoQualities(),
			entity.getTranscodeProgress(),
			entity.getVideoTranscodeStatus(),
			entity.getCreatedAt(),
			entity.getUpdateAt()
		);
	}
}
