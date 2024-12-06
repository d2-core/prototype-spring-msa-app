package com.d2.authservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KakaoUserInfoDto {
	private Long id;

	@JsonProperty("kakao_account")
	private KakaoAccount kakaoAccount;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class KakaoAccount {
		private String email;

		@JsonProperty("phone_number")
		private String phoneNumber;

		private Profile profile;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Profile {
		private String nickname;

		@JsonProperty("profile_image_url")
		private String profileImageUrl;
	}
}
