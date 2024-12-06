package com.d2.authservice.adapter.out.persistence.user;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.d2.authservice.model.enums.SocialCategory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Table(name = "user_social_profiles")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class UserSocialProfileJpaEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 255, nullable = false, unique = true)
	private String socialProfileId;

	@Column(length = 50, nullable = false)
	@Enumerated(EnumType.STRING)
	private SocialCategory socialCategory;

	@Setter
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UserJpaEntity userJpaEntity;
	
	public UserSocialProfileJpaEntity(String socialProfileId, SocialCategory socialCategory) {
		this.socialProfileId = socialProfileId;
		this.socialCategory = socialCategory;
	}

	public UserSocialProfileJpaEntity update(String socialProfileId, SocialCategory socialCategory) {
		this.socialProfileId = socialProfileId;
		this.socialCategory = socialCategory;

		return this;
	}
}
