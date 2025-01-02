package com.d2.core.application.port.out;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ObjectStoragePort {

	String uploadImage(MultipartFile imageFile);

	String uploadImage(String preUrl, MultipartFile imageFile);

	List<String> uploadImages(List<MultipartFile> imageFiles);

	List<String> uploadImages(List<String> preImageUrls, List<MultipartFile> imageFiles);
}
