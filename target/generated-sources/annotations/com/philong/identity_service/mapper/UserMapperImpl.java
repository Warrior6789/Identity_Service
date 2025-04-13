package com.philong.identity_service.mapper;

import com.philong.identity_service.entity.User;
import com.philong.identity_service.request.UserCreationRequest;
import com.philong.identity_service.request.UserUpdateRequest;
import com.philong.identity_service.response.UserResponse;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-13T14:45:29+0700",
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
        Set<String> set = user.getRole();
        if ( set != null ) {
            userResponse.role( new LinkedHashSet<String>( set ) );
        }

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
}
