package com.d2.core.application.port.out;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ObjectStoragePort {
	String uploadFile(MultipartFile file);

	List<String> uploadFiles(List<MultipartFile> files);

	String deleteFile(String fileUrl);

	List<String> deleteFiles(List<String> fileUrls);
}
