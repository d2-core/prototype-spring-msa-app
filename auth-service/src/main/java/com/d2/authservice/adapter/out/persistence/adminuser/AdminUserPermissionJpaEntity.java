package com.d2.authservice.adapter.out.persistence.adminuser;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.d2.authservice.model.enums.AdminUserPermission;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "admin_user_permissions")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AdminUserPermissionJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "admin_user_id", insertable = false, updatable = false)
	private Long adminUserId;

	@Column(length = 50, nullable = false)
	@Enumerated(EnumType.STRING)
	private AdminUserPermission permission;

	@CreatedDate
	private LocalDateTime createdAt;

	public AdminUserPermissionJpaEntity(Long adminUserId, AdminUserPermission permission) {
		this.adminUserId = adminUserId;
		this.permission = permission;
	}
}
