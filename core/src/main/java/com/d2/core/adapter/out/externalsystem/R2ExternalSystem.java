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
import com.d2.core.application.port.out.ObjectStoragePort;
import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiExceptionImpl;
import com.d2.core.utils.ImageUrlHelper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class R2ExternalSystem implements ObjectStoragePort {
	private final AmazonS3 amazonS3;

	@Value("${aws.s3.bucketName:null}")
	private String bucketName;

	@Override
	public String uploadImage(MultipartFile imageFile) {
		return uploadFile(imageFile);
	}

	@Override
	public String uploadImage(String preUrl, MultipartFile imageFile) {
		if (preUrl != null) {
			String newImageUrl = uploadFile(imageFile);

			String key = extractKeyFromUrl(preUrl);
			amazonS3.deleteObject(bucketName, key);
			return newImageUrl;
		} else {
			return uploadFile(imageFile);
		}
	}

	@Override
	public List<String> uploadImages(List<MultipartFile> imageFiles) {
		return imageFiles.stream().map(this::uploadFile).collect(Collectors.toList());
	}

	@Override
	public List<String> uploadImages(List<String> preImageUrls, List<MultipartFile> imageFiles) {
		if (bucketName == null) {
			throw new ApiExceptionImpl(ErrorCodeImpl.INTERNAL_SERVER_ERROR, "r2 bucket is null");
		}

		if (preImageUrls != null && !imageFiles.isEmpty()) {
			List<String> newUploadedUrls = imageFiles.stream().map(this::uploadFile).collect(Collectors.toList());

			preImageUrls.forEach(url -> {
				String key = extractKeyFromUrl(url);
				amazonS3.deleteObject(bucketName, key);
			});

			return newUploadedUrls;
		} else {
			return imageFiles.stream().map(this::uploadFile).collect(Collectors.toList());
		}
	}

	private String uploadFile(MultipartFile file) {
		if (bucketName == null) {
			throw new ApiExceptionImpl(ErrorCodeImpl.INTERNAL_SERVER_ERROR, "r2 bucket is null");
		}
		String prefix = ImageUrlHelper.objectStorePrefix;
		String uniqueFileKey =
			prefix + "-" + LocalDate.now().format(DateTimeFormatter.ISO_DATE) + "-" + UUID.randomUUID();

		try (InputStream inputStream = file.getInputStream()) {
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(file.getSize());
			metadata.setContentType(file.getContentType());

			amazonS3.putObject(bucketName, uniqueFileKey, inputStream, metadata);
		} catch (Exception e) {
			throw new ApiExceptionImpl(ErrorCodeImpl.BAG_GATEWAY,
				"fileSize=%s, contentType=%s".formatted(file.getSize(), file.getContentType()));
		}

		return uniqueFileKey;
	}

	private String extractKeyFromUrl(String url) {
		String bucketUrl = String.format("https://%s.s3.amazonaws.com/", bucketName);
		if (url.startsWith(bucketUrl)) {
			return url.substring(bucketUrl.length());
		}
		throw new ApiExceptionImpl(ErrorCodeImpl.INTERNAL_SERVER_ERROR, "Invalid S3 URL: " + url);
	}
}
