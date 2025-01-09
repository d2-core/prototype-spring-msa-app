package com.d2.core.utils;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiExceptionImpl;

@Component
public class FileManager {

	public File writeMutipartFileTempFile(String prefix, String suffix, MultipartFile multipartFile) {
		try {
			File tempInputFile = File.createTempFile(prefix, suffix);
			multipartFile.transferTo(tempInputFile);
			return tempInputFile;
		} catch (IOException e) {
			throw new ApiExceptionImpl(ErrorCodeImpl.INTERNAL_SERVER_ERROR, e);
		}
	}

	public void cleanup(File mulipartTempInputFile) {
		mulipartTempInputFile.delete();
	}
}
