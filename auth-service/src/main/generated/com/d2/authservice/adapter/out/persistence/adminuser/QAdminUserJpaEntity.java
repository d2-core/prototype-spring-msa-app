package com.d2.authservice.adapter.out.persistence.adminuser;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAdminUserJpaEntity is a Querydsl query type for AdminUserJpaEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAdminUserJpaEntity extends EntityPathBase<AdminUserJpaEntity> {

    private static final long serialVersionUID = 1475051194L;

    public static final QAdminUserJpaEntity adminUserJpaEntity = new QAdminUserJpaEntity("adminUserJpaEntity");

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> lastLoginAt = createDateTime("lastLoginAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> modifiedAt = createDateTime("modifiedAt", java.time.LocalDateTime.class);

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final ListPath<AdminUserPermissionJpaEntity, QAdminUserPermissionJpaEntity> permissions = this.<AdminUserPermissionJpaEntity, QAdminUserPermissionJpaEntity>createList("permissions", AdminUserPermissionJpaEntity.class, QAdminUserPermissionJpaEntity.class, PathInits.DIRECT2);

    public final StringPath phoneNumber = createString("phoneNumber");

    public final DateTimePath<java.time.LocalDateTime> registeredAt = createDateTime("registeredAt", java.time.LocalDateTime.class);

    public final EnumPath<com.d2.core.model.enums.Role> role = createEnum("role", com.d2.core.model.enums.Role.class);

    public final EnumPath<com.d2.authservice.model.enums.AdminUserStatus> status = createEnum("status", com.d2.authservice.model.enums.AdminUserStatus.class);

    public QAdminUserJpaEntity(String variable) {
        super(AdminUserJpaEntity.class, forVariable(variable));
    }

    public QAdminUserJpaEntity(Path<? extends AdminUserJpaEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAdminUserJpaEntity(PathMetadata metadata) {
        super(AdminUserJpaEntity.class, metadata);
    }

}
