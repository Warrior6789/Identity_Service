package com.philong.identity_service.service;

import com.philong.identity_service.request.UserCreationRequest;
import com.philong.identity_service.request.UserUpdateRequest;
import com.philong.identity_service.response.UserResponse;

import java.util.List;

public interface IUserService {
    UserResponse getUserById(String id);
    List<UserResponse> getAllUser();
    UserResponse addUser(UserCreationRequest request);
    UserResponse updateUser(UserUpdateRequest request, String id);
    void deleteUser(String id);
    UserResponse getMyInfo();
}
