package com.d2.productservice.adapter.out.persistence.statics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StaticJpaRepository extends JpaRepository<StaticJpaEntity, Long> {
	@Query("select count(e) from StaticJpaEntity e")
	Long countAll();
}
