package com.d2.authservice.application.port.out;

import com.d2.authservice.model.dto.KakaoUserInfoDto;

public interface KakaoPort {
	KakaoUserInfoDto getUserInfo(String code);
}
