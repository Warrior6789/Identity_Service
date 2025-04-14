package com.philong.identity_service.mapper;

import com.philong.identity_service.entity.Permission;
import com.philong.identity_service.entity.Role;
import com.philong.identity_service.request.RoleCreationRequest;
import com.philong.identity_service.response.RoleResponse;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-14T17:24:32+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class RoleMapperImpl implements RoleMapper {

    @Override
    public Role toRole(RoleCreationRequest request) {
        if ( request == null ) {
            return null;
        }

        Role.RoleBuilder role = Role.builder();

        role.name( request.getName() );
        role.description( request.getDescription() );

        return role.build();
    }

    @Override
    public RoleResponse toRoleResponse(Role role) {
        if ( role == null ) {
            return null;
        }

        RoleResponse.RoleResponseBuilder roleResponse = RoleResponse.builder();

        roleResponse.name( role.getName() );
        roleResponse.description( role.getDescription() );
        Set<Permission> set = role.getPermissions();
        if ( set != null ) {
            roleResponse.permissions( new LinkedHashSet<Permission>( set ) );
        }

        return roleResponse.build();
    }

    @Override
    public List<RoleResponse> toRoleResponses(List<Role> roles) {
        if ( roles == null ) {
            return null;
        }

        List<RoleResponse> list = new ArrayList<RoleResponse>( roles.size() );
        for ( Role role : roles ) {
            list.add( toRoleResponse( role ) );
        }

        return list;
    }
}
