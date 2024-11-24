package com.d2.authservice.application.port.out;

import java.util.Map;

import com.d2.authservice.model.dto.TokenDto;

public interface TokenPort {

	TokenDto issueAccessToken(Map<String, Object> data);

	TokenDto issueRefreshToken(Map<String, Object> data);

	Map<String, Object> validateTokenWithThrow(String jwtToken);
}
