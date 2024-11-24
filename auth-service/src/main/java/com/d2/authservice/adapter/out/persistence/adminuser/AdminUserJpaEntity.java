package com.d2.authservice.adapter.out.persistence.adminuser;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.d2.authservice.model.enums.AdminUserStatus;
import com.d2.core.model.enums.Role;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "admin_users")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AdminUserJpaEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 50, nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "admin_user_id", nullable = false)
	private List<AdminUserPermissionJpaEntity> permissions = List.of();

	@Column(length = 50, nullable = false)
	private String name;

	@Column(length = 100, nullable = false, unique = true)
	private String email;

	@Column(length = 100, nullable = false)
	private String password;

	@Column(length = 100, unique = true)
	private String phoneNumber;

	@Column(length = 50, nullable = false)
	@Enumerated(EnumType.STRING)
	private AdminUserStatus status;

	@CreatedDate
	private LocalDateTime registeredAt;

	@LastModifiedDate
	private LocalDateTime modifiedAt;

	private LocalDateTime lastLoginAt;

	public AdminUserJpaEntity(Role role, String name, String email, String password, String phoneNumber,
		AdminUserStatus status, LocalDateTime lastLoginAt) {
		this.role = role;
		this.name = name;
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.status = status;
		this.lastLoginAt = lastLoginAt;
	}

	public void addAdminUserPermissionJpaEntity(AdminUserPermissionJpaEntity permission) {
		this.permissions = List.of(permission);
	}
}
