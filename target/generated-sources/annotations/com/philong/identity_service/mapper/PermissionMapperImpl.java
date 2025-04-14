package com.philong.identity_service.mapper;

import com.philong.identity_service.entity.Permission;
import com.philong.identity_service.request.PermissionCreationRequest;
import com.philong.identity_service.request.PermissionUpdateRequest;
import com.philong.identity_service.response.PermissionResponse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-14T17:57:59+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class PermissionMapperImpl implements PermissionMapper {

    @Override
    public Permission toPermission(PermissionCreationRequest permissionCreationRequest) {
        if ( permissionCreationRequest == null ) {
            return null;
        }

        Permission.PermissionBuilder permission = Permission.builder();

        permission.name( permissionCreationRequest.getName() );
        permission.description( permissionCreationRequest.getDescription() );

        return permission.build();
    }

    @Override
    public PermissionResponse toPermissionResponse(Permission permission) {
        if ( permission == null ) {
            return null;
        }

        PermissionResponse.PermissionResponseBuilder permissionResponse = PermissionResponse.builder();

        permissionResponse.name( permission.getName() );
        permissionResponse.description( permission.getDescription() );

        return permissionResponse.build();
    }

    @Override
    public List<PermissionResponse> toPermissionResponses(List<Permission> permissions) {
        if ( permissions == null ) {
            return null;
        }

        List<PermissionResponse> list = new ArrayList<PermissionResponse>( permissions.size() );
        for ( Permission permission : permissions ) {
            list.add( toPermissionResponse( permission ) );
        }

        return list;
    }

    @Override
    public Permission toUpdatePermission(Permission permission, PermissionUpdateRequest request) {
        if ( request == null ) {
            return permission;
        }

        permission.setName( request.getName() );

        return permission;
    }
}
