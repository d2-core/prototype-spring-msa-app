package com.d2.productservice.adapter.out.persistence.video;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.d2.core.adapter.out.converter.StringListConverter;
import com.d2.core.model.enums.VideoResolution;
import com.d2.productservice.model.enums.VideoTranscodeStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "video_streams")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class VideoSteamJpaEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String videoUniqueKey;

	@Column(nullable = false)
	private String videoUrl;

	@Column(nullable = false)
	private Integer duration;

	@Column(nullable = false)
	private String videoFormat;

	@Column(nullable = false)
	private Long originalVideoSize;

	@Convert(converter = VideoResolutionListConverter.class)
	@Column(nullable = false, columnDefinition = "text")
	private List<VideoResolution> supportedVideoResolution;

	@Convert(converter = StringListConverter.class)
	@Column(nullable = false, columnDefinition = "text")
	private List<String> originalSupportableVideoQualities;

	@Column(nullable = false)
	private Integer transcodeProgress;

	@Enumerated(EnumType.STRING)
	@Column(length = 50, columnDefinition = "varchar(50)")
	private VideoTranscodeStatus videoTranscodeStatus;

	@CreatedDate
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime updateAt;

	public VideoSteamJpaEntity(Long id) {
		this.id = id;
	}

	public VideoSteamJpaEntity(String videoUniqueKey, Integer duration, String videoFormat, Long originalVideoSize,
		List<VideoResolution> supportedVideoResolution, List<String> originalSupportableVideoQualities,
		VideoTranscodeStatus videoTranscodeStatus) {
		this.videoUniqueKey = videoUniqueKey;
		this.videoUrl = "";
		this.duration = duration;
		this.videoFormat = videoFormat;
		this.originalVideoSize = originalVideoSize;
		this.supportedVideoResolution = supportedVideoResolution;
		this.originalSupportableVideoQualities = originalSupportableVideoQualities;
		this.transcodeProgress = 0;
		this.videoTranscodeStatus = videoTranscodeStatus;
	}

	public VideoSteamJpaEntity update(VideoTranscodeStatus videoTranscodeStatus) {
		this.videoTranscodeStatus = videoTranscodeStatus;
		return this;
	}

	public VideoSteamJpaEntity update(String videoUrl, Integer transcodeProgress,
		VideoTranscodeStatus videoTranscodeStatus) {
		this.videoUrl = videoUrl;
		this.transcodeProgress = transcodeProgress;
		this.videoTranscodeStatus = videoTranscodeStatus;
		return this;
	}

	public VideoSteamJpaEntity update(Integer transcodeProgress, VideoTranscodeStatus videoTranscodeStatus) {
		this.transcodeProgress = transcodeProgress;
		this.videoTranscodeStatus = videoTranscodeStatus;

		return this;
	}
}
