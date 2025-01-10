package com.d2.productservice.model.dto;

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
	private Long foreignId;
	private String videoUrl;
	private Integer duration;
	private List<VideoResolution> supportedVideoResolutions;
	private VideoTranscodeStatus videoTranscodeStatus;
	private Integer transcodeProgress;

	public static VideoStreamDto from(VideoSteamJpaEntity videoSteamJpaEntity) {
		return new VideoStreamDto(
			videoSteamJpaEntity.getId(),
			null,
			videoSteamJpaEntity.getVideoUrl(),
			videoSteamJpaEntity.getDuration(),
			videoSteamJpaEntity.getSupportedVideoResolutions(),
			videoSteamJpaEntity.getVideoTranscodeStatus(),
			videoSteamJpaEntity.getTranscodeProgress()
		);
	}
}
