package com.philong.identity_service.mapper;

import com.philong.identity_service.entity.Permission;
import com.philong.identity_service.entity.User;
import com.philong.identity_service.request.PermissionCreationRequest;
import com.philong.identity_service.request.PermissionUpdateRequest;
import com.philong.identity_service.request.UserCreationRequest;
import com.philong.identity_service.request.UserUpdateRequest;
import com.philong.identity_service.response.PermissionResponse;
import com.philong.identity_service.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionCreationRequest permissionCreationRequest);
    PermissionResponse toPermissionResponse(Permission permission);
    List<PermissionResponse> toPermissionResponses(List<Permission> permissions);
    Permission toUpdatePermission(@MappingTarget Permission permission, PermissionUpdateRequest request);
}
