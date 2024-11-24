package com.d2.authservice.adapter.out.persistence.adminuser;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAdminUserPermissionJpaEntity is a Querydsl query type for AdminUserPermissionJpaEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAdminUserPermissionJpaEntity extends EntityPathBase<AdminUserPermissionJpaEntity> {

    private static final long serialVersionUID = -1147421013L;

    public static final QAdminUserPermissionJpaEntity adminUserPermissionJpaEntity = new QAdminUserPermissionJpaEntity("adminUserPermissionJpaEntity");

    public final NumberPath<Long> adminUserId = createNumber("adminUserId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.d2.authservice.model.enums.AdminUserPermission> permission = createEnum("permission", com.d2.authservice.model.enums.AdminUserPermission.class);

    public QAdminUserPermissionJpaEntity(String variable) {
        super(AdminUserPermissionJpaEntity.class, forVariable(variable));
    }

    public QAdminUserPermissionJpaEntity(Path<? extends AdminUserPermissionJpaEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAdminUserPermissionJpaEntity(PathMetadata metadata) {
        super(AdminUserPermissionJpaEntity.class, metadata);
    }

}

