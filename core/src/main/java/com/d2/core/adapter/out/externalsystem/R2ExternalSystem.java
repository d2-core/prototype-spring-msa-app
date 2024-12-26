package com.d2.core.adapter.out.externalsystem;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.d2.core.application.port.out.StoragePort;
import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiExceptionImpl;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class R2ExternalSystem implements StoragePort {
	private final AmazonS3 amazonS3;

	@Value("${aws.s3.bucketName}")
	private String bucketName;

	@Override
	public String uploadImage(MultipartFile imageFile) {
		return uploadFile(imageFile);
	}

	@Override
	public List<String> uploadImages(List<MultipartFile> imageFiles) {
		return imageFiles.stream().map(this::uploadFile).collect(Collectors.toList());
	}

	@Override
	public String uploadVideo(MultipartFile videoFile) {
		return null;
	}

	private String uploadFile(MultipartFile file) {
		String uniqueFileKey = LocalDate.now().format(DateTimeFormatter.ISO_DATE) + "-" + UUID.randomUUID();

		try (InputStream inputStream = file.getInputStream()) {
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(file.getSize());
			metadata.setContentType(file.getContentType());

			amazonS3.putObject(bucketName, uniqueFileKey, inputStream, metadata);
		} catch (Exception e) {
			throw new ApiExceptionImpl(ErrorCodeImpl.BAG_GATEWAY,
				"fileSize=%s, contentType=%s".formatted(file.getSize(), file.getContentType()));
		}

		return amazonS3.getUrl(bucketName, uniqueFileKey).toString();
	}
}
