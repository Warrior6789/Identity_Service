package com.philong.identity_service.service;

import com.philong.identity_service.entity.Role;
import com.philong.identity_service.entity.User;
import com.philong.identity_service.exception.AppException;
import com.philong.identity_service.exception.Error;
import com.philong.identity_service.mapper.UserMapper;
import com.philong.identity_service.repository.RoleRepository;
import com.philong.identity_service.repository.UserRepository;
import com.philong.identity_service.request.UserCreationRequest;
import com.philong.identity_service.request.UserUpdateRequest;
import com.philong.identity_service.response.UserResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService implements IUserService {

     UserRepository userRepository;
     UserMapper userMapper;
     PasswordEncoder passwordEncoder;
     RoleRepository roleRepository;

    @Override
    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUserById(String id) {
        User user = userRepository.findById(id).
                orElseThrow(() -> new AppException(Error.USER_NOT_EXIST));
        return userMapper.toUserResponse(user);
    }

    @Override
    @PreAuthorize("hasAuthority('MANAGE_USER')")
    public List<UserResponse> getAllUser() {
        List<User> list = userRepository.findAll();
        return userMapper.toUserResponses(list);
    }

    @Override
    @PreAuthorize("hasAuthority('MANAGE_USER')")

    public UserResponse addUser(UserCreationRequest request) {
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        var defaultRole = roleRepository.findById("USER").orElseThrow(() -> new AppException(Error.ROLE_NOT_EXIST));
        HashSet<Role> roles = new HashSet<>();
        roles.add(defaultRole);
        user.setRoles(roles);
        user = userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    @Override
    @PreAuthorize("hasAuthority('MANAGE_USER')")

    public UserResponse updateUser(UserUpdateRequest request, String id) {
        User user = userRepository.findById(id).
                orElseThrow(() -> new AppException(Error.USER_NOT_EXIST));
        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));
        return userMapper.toUserResponse( userRepository.save(user));
    }

    @Override
    @PreAuthorize("hasAuthority('MANAGE_USER')")

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    @Override

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getMyInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(Error.USER_NOT_EXIST));
        return userMapper.toUserResponse(user);
    }
}


// postAuthorize kiểm tra quyền sau khi method thực thi
// returnObject là biến đặc bịệt trong postAuthorize ta có thể truy cập các thuộc tính
// và phương thức của đối tượng trả về này trong biểu thức spel

// updateRequest chứa list các role kiểu string và từ các role kiểu string t dùng findAllById để lấy ra list các Role
