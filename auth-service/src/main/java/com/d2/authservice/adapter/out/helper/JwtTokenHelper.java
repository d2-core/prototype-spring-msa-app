package com.d2.authservice.adapter.out.helper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.d2.authservice.application.port.out.TokenPort;
import com.d2.authservice.error.TokenErrorCodeImpl;
import com.d2.authservice.model.dto.TokenDto;
import com.d2.core.exception.ApiExceptionImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtTokenHelper implements TokenPort {

	@Value("${token.secret.key}")
	private String secretKey;

	@Value("${token.access-token.plus-hour}")
	private Long accessTokenPlusHour;

	@Value("${token.refresh-token.plus-hour}")
	private Long refreshTokenPlusHour;

	@Override
	public TokenDto issueAccessToken(Map<String, Object> data) {
		LocalDateTime expiredLocalDateTime = LocalDateTime.now().plusHours(accessTokenPlusHour);
		return issueToken(data, expiredLocalDateTime);
	}

	@Override
	public TokenDto issueRefreshToken(Map<String, Object> data) {
		LocalDateTime expiredLocalDateTime = LocalDateTime.now().plusHours(refreshTokenPlusHour);
		return issueToken(data, expiredLocalDateTime);
	}

	@Override
	public Map<String, Object> validateTokenWithThrow(String jwtToken) {
		if (jwtToken == null || jwtToken.isBlank()) {
			throw new ApiExceptionImpl(TokenErrorCodeImpl.TOKEN_NOT_FOUND, "jwtToken: %s".formatted(jwtToken));
		}

		SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
		JwtParser parser = Jwts.parserBuilder()
			.setSigningKey(key)
			.build();
		try {
			Jws<Claims> result = parser.parseClaimsJws(jwtToken);
			return new HashMap<>(result.getBody());
		} catch (SignatureException ex) {
			throw new ApiExceptionImpl(TokenErrorCodeImpl.INVALID_TOKEN, "jwtToken: %s".formatted(jwtToken));
		} catch (ExpiredJwtException ex) {
			throw new ApiExceptionImpl(TokenErrorCodeImpl.EXPIRED_TOKEN, "jwtToken: %s".formatted(jwtToken));
		} catch (Exception e) {
			throw new ApiExceptionImpl(TokenErrorCodeImpl.INTERNAL_TOKEN_ERROR, "jwtToken: %s".formatted(jwtToken));
		}
	}

	public TokenDto issueToken(Map<String, Object> data, LocalDateTime expiredLocalDateTime) {
		Date expiredAt = Date.from(expiredLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());

		byte[] keyBytes = secretKey.getBytes();
		var key = Keys.hmacShaKeyFor(keyBytes);

		String jwtToken = Jwts.builder()
			.signWith(key, SignatureAlgorithm.HS256)
			.setClaims(data)
			.setExpiration(expiredAt)
			.compact();

		return TokenDto.builder()
			.token(jwtToken)
			.expiredAt(expiredLocalDateTime)
			.build();
	}
}
