package com.d2.productservice.application.port.out;

import com.d2.core.model.dto.VideoConvertDto;
import com.d2.productservice.model.dto.VideoStreamDto;
import com.d2.productservice.model.enums.VideoTranscodeStatus;

public interface VideoStreamPort {

	VideoStreamDto register(VideoConvertDto videoConvertDto);

	VideoStreamDto update(Long videoStreamId, String videoUrl, Integer progress,
		VideoTranscodeStatus videoTranscodeStatus);

	VideoStreamDto update(Long videoStreamId, Integer progress, VideoTranscodeStatus videoTranscodeStatus);

	VideoStreamDto update(Long videoStreamId, VideoTranscodeStatus videoTranscodeStatus);

	VideoStreamDto getVideoStream(Long videoStream);

	void delete(Long videoStreamId);

	void deleteByVideoUniqueKey(String videoUniqueKey);
}
