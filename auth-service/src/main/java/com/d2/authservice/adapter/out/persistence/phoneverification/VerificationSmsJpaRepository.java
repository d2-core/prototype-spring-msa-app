package com.d2.authservice.adapter.out.persistence.phoneverification;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationSmsJpaRepository extends JpaRepository<VerificationSmsJpaEntity, Long> {

}