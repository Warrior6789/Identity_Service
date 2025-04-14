package com.philong.identity_service.service;

import com.philong.identity_service.entity.Permission;
import com.philong.identity_service.request.PermissionCreationRequest;
import com.philong.identity_service.request.PermissionUpdateRequest;
import com.philong.identity_service.response.PermissionResponse;

import java.util.List;

public interface IPermissionService {
    PermissionResponse create(PermissionCreationRequest request);
    List<PermissionResponse> getAll();
    void delete(String permissionName);
}
