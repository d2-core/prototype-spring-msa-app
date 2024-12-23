package com.d2.authservice.application.service;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.d2.authservice.application.port.in.UserAuthUseCase;
import com.d2.authservice.application.port.out.KakaoPort;
import com.d2.authservice.application.port.out.TokenPort;
import com.d2.authservice.application.port.out.UserPort;
import com.d2.authservice.constant.TokenConstant;
import com.d2.authservice.model.domain.UserLogin;
import com.d2.authservice.model.dto.KakaoUserInfoDto;
import com.d2.authservice.model.dto.TokenDto;
import com.d2.authservice.model.dto.UserDto;
import com.d2.authservice.model.enums.SocialCategory;
import com.d2.authservice.model.enums.UserStatus;
import com.d2.core.model.enums.TokenRole;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class UserAuthService implements UserAuthUseCase {
	private final KakaoPort kakaoPort;
	private final UserPort userPort;
	private final TokenPort tokenPort;

	@Override
	public UserLogin loginWithKakao(String code) {
		KakaoUserInfoDto kakaoUserInfoDto = kakaoPort.getUserInfo(code);

		String socialProfileId = String.valueOf(kakaoUserInfoDto.getId());
		UserDto userDto = userPort.upsertBySocialProfileId(
			socialProfileId,
			kakaoUserInfoDto.getKakaoAccount().getProfile().getNickname(),
			kakaoUserInfoDto.getKakaoAccount().getEmail(),
			kakaoUserInfoDto.getKakaoAccount().getPhoneNumber(),
			UserStatus.REGISTERED,
			LocalDateTime.now(),
			SocialCategory.KAKAO);

		Map<String, Object> data = Map.of(
			TokenConstant.ROLE, TokenRole.APP.name(),
			TokenConstant.ID, String.valueOf(userDto.getId())
		);

		TokenDto accessToken = tokenPort.issueAccessToken(data);
		TokenDto refreshToken = tokenPort.issueRefreshToken(data);

		return UserLogin.from(userDto, accessToken, refreshToken);
	}
}
