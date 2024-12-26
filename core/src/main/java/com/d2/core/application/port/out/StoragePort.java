package com.d2.core.application.port.out;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface StoragePort {

	String uploadImage(MultipartFile imageFile);

	List<String> uploadImages(List<MultipartFile> imageFiles);

	String uploadVideo(MultipartFile videoFile);
}
