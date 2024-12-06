package com.d2.authservice.adapter.out.persistence.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSocialProfileJpaRepository extends JpaRepository<UserSocialProfileJpaEntity, Long> {
}
