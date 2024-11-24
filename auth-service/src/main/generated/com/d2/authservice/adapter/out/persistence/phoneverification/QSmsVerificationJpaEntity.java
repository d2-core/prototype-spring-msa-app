package com.d2.authservice.adapter.out.persistence.phoneverification;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSmsVerificationJpaEntity is a Querydsl query type for SmsVerificationJpaEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSmsVerificationJpaEntity extends EntityPathBase<SmsVerificationJpaEntity> {

    private static final long serialVersionUID = -577624303L;

    public static final QSmsVerificationJpaEntity smsVerificationJpaEntity = new QSmsVerificationJpaEntity("smsVerificationJpaEntity");

    public final EnumPath<com.d2.authservice.model.enums.SmsAuthenticationCategory> category = createEnum("category", com.d2.authservice.model.enums.SmsAuthenticationCategory.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath phoneNumber = createString("phoneNumber");

    public final StringPath smsAuthCode = createString("smsAuthCode");

    public final BooleanPath verified = createBoolean("verified");

    public QSmsVerificationJpaEntity(String variable) {
        super(SmsVerificationJpaEntity.class, forVariable(variable));
    }

    public QSmsVerificationJpaEntity(Path<? extends SmsVerificationJpaEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSmsVerificationJpaEntity(PathMetadata metadata) {
        super(SmsVerificationJpaEntity.class, metadata);
    }

}

