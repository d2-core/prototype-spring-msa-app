package com.d2.productservice.adapter.out.persistence.statics;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.d2.core.adapter.out.move.MoveAble;
import com.d2.productservice.model.enums.StaticCategory;

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
import lombok.Setter;

@Getter
@Table(name = "statics")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class StaticJpaEntity implements MoveAble {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private StaticCategory staticCategory;

	@Column(length = 255, nullable = false)
	private String name;

	@Column(length = 1024, nullable = true)
	private String description;

	@Setter
	private Long orders;

	@CreatedDate
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime updatedAt;

	public StaticJpaEntity(StaticCategory staticCategory, String name, String description, Long orders) {
		this.staticCategory = staticCategory;
		this.name = name;
		this.description = description;
		this.orders = orders;
	}

	public StaticJpaEntity modify(String name, String description) {
		this.name = name;
		this.description = description;

		return this;
	}
}
