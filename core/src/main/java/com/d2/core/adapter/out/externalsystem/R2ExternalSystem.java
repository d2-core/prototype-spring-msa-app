package com.d2.core.adapter.out.externalsystem;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class R2ExternalSystem implements ObjectStoragePort {
	private final AmazonS3 amazonS3;

	@Value("${aws.s3.bucketName:null}")
	private String bucketName;

	@Value("${url.r2:null}")
	private String r2Url;

	@Value("${aws.s3.urlPrefix:null}")
	private String urlPrefix;

	@Override
	public String uploadFile(MultipartFile file) {
		validateSettings();

		if (file == null) {
			return "";
		}

		long maxFileBites = 5L * 1024 * 1024;
		if (file.getSize() > maxFileBites) {
			throw new ApiExceptionImpl(ErrorCodeImpl.BAG_GATEWAY, "지원하는 파일 사이즈 초과");
		}

		String originalFilename = file.getOriginalFilename();
		String fileExtension = "";
		if (originalFilename != null && originalFilename.contains(".")) {
			fileExtension = "." + originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
		}

		String uniqueFileKey =
			urlPrefix + "/" + LocalDate.now().format(DateTimeFormatter.ISO_DATE) + "/" + UUID.randomUUID()
				+ fileExtension;

		try (InputStream inputStream = file.getInputStream()) {
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(file.getSize());
			metadata.setContentType(file.getContentType());

			amazonS3.putObject(bucketName, uniqueFileKey, inputStream, metadata);
		} catch (Exception e) {
			throw new ApiExceptionImpl(ErrorCodeImpl.BAG_GATEWAY,
				"fileSize=%s, contentType=%s".formatted(file.getSize(), file.getContentType()));
		}

		return r2Url + "/" + uniqueFileKey;
	}

	@Override
	public List<String> uploadFiles(List<MultipartFile> files) {
		return files.stream().map(this::uploadFile).collect(Collectors.toList());
	}

	@Override
	public String deleteFile(String fileUrl) {
		validateSettings();

		if (fileUrl == null || fileUrl.isEmpty()) {
			return "";
		}

		try {
			String key = extractKeyFromUrl(fileUrl);
			amazonS3.deleteObject(bucketName, key);

			return fileUrl;
		} catch (Exception e) {
			throw new ApiExceptionImpl(ErrorCodeImpl.BAG_GATEWAY,
				"Error deleting video: " + e.getMessage());
		}
	}

	@Override
	public List<String> deleteFiles(List<String> fileUrls) {
		if (fileUrls == null || fileUrls.isEmpty()) {
			return new ArrayList<>();
		}
		return fileUrls.stream().map(this::deleteFile).collect(Collectors.toList());
	}

	private void validateSettings() {
		if (bucketName == null || r2Url == null || urlPrefix == null) {
			throw new ApiExceptionImpl(ErrorCodeImpl.INTERNAL_SERVER_ERROR, "r2 bucket is null");
		}
	}

	private String extractKeyFromUrl(String fileUrl) {
		try {
			int startIndex = fileUrl.indexOf(urlPrefix);
			if (startIndex != -1) {
				return fileUrl.substring(startIndex);
			}
			throw new IllegalArgumentException("URL does not contain the specified prefix");
		} catch (Exception e) {
			throw new IllegalArgumentException("Could not extract key from URL: " + fileUrl);
		}
	}
}
