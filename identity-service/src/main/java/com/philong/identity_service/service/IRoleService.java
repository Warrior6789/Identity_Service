package com.philong.identity_service.service;

import com.philong.identity_service.request.PermissionCreationRequest;
import com.philong.identity_service.request.RoleCreationRequest;
import com.philong.identity_service.response.PermissionResponse;
import com.philong.identity_service.response.RoleResponse;

import java.util.List;

public interface IRoleService {
    RoleResponse create(RoleCreationRequest request);
    List<RoleResponse> getAll();
    void delete(String roleName);
}
