package com.philong.identity_service.mapper;

import com.philong.identity_service.entity.Permission;
import com.philong.identity_service.entity.Role;
import com.philong.identity_service.entity.User;
import com.philong.identity_service.request.UserCreationRequest;
import com.philong.identity_service.request.UserUpdateRequest;
import com.philong.identity_service.response.RoleResponse;
import com.philong.identity_service.response.UserResponse;
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
public class UserMapperImpl implements UserMapper {

    @Override
    public User toUser(UserCreationRequest user) {
        if ( user == null ) {
            return null;
        }

        User.UserBuilder user1 = User.builder();

        user1.username( user.getUsername() );
        user1.password( user.getPassword() );
        user1.first_name( user.getFirst_name() );
        user1.last_name( user.getLast_name() );
        user1.dob( user.getDob() );
        user1.email( user.getEmail() );

        return user1.build();
    }

    @Override
    public UserResponse toUserResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.id( user.getId() );
        userResponse.username( user.getUsername() );
        userResponse.first_name( user.getFirst_name() );
        userResponse.last_name( user.getLast_name() );
        userResponse.dob( user.getDob() );
        userResponse.email( user.getEmail() );
        userResponse.roles( roleSetToRoleResponseSet( user.getRoles() ) );

        return userResponse.build();
    }

    @Override
    public List<UserResponse> toUserResponses(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserResponse> list = new ArrayList<UserResponse>( users.size() );
        for ( User user : users ) {
            list.add( toUserResponse( user ) );
        }

        return list;
    }

    @Override
    public void updateUser(User user, UserUpdateRequest request) {
        if ( request == null ) {
            return;
        }

        user.setPassword( request.getPassword() );
        user.setFirst_name( request.getFirst_name() );
        user.setLast_name( request.getLast_name() );
        user.setDob( request.getDob() );
        user.setEmail( request.getEmail() );
    }

    protected RoleResponse roleToRoleResponse(Role role) {
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

    protected Set<RoleResponse> roleSetToRoleResponseSet(Set<Role> set) {
        if ( set == null ) {
            return null;
        }

        Set<RoleResponse> set1 = new LinkedHashSet<RoleResponse>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Role role : set ) {
            set1.add( roleToRoleResponse( role ) );
        }

        return set1;
    }
}
