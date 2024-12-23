package com.d2.authservice.adapter.out.persistence.user;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.d2.authservice.model.enums.UserStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "users")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class UserJpaEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 50, nullable = false)
	private String nickname;

	@Column(length = 100, nullable = false, unique = true)
	private String email;

	@Column(length = 100, unique = true)
	private String phoneNumber;

	@Column(length = 50, nullable = false)
	@Enumerated(EnumType.STRING)
	private UserStatus status;

	@CreatedDate
	private LocalDateTime registeredAt;

	@LastModifiedDate
	private LocalDateTime modifiedAt;

	private LocalDateTime lastLoginAt;

	@OneToOne(mappedBy = "userJpaEntity", fetch = FetchType.LAZY)
	private UserSocialProfileJpaEntity userSocialProfileJpaEntity;

	public void setUserSocialProfileJpaEntity(UserSocialProfileJpaEntity userSocialProfileJpaEntity) {
		this.userSocialProfileJpaEntity = userSocialProfileJpaEntity;
		if (userSocialProfileJpaEntity.getUserJpaEntity() != this) {
			userSocialProfileJpaEntity.setUserJpaEntity(this);
		}
	}

	public UserJpaEntity(String nickname, String email, String phoneNumber, UserStatus status,
		LocalDateTime lastLoginAt) {
		this.nickname = nickname;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.status = status;
		this.lastLoginAt = lastLoginAt;
	}

	public UserJpaEntity update(String nickname, String email, String phoneNumber, UserStatus status,
		LocalDateTime lastLoginAt) {
		this.nickname = nickname;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.status = status;
		this.lastLoginAt = lastLoginAt;

		return this;
	}
}
