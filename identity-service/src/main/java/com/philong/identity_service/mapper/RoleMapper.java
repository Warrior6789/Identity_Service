package com.philong.identity_service.mapper;

import com.philong.identity_service.entity.Role;
import com.philong.identity_service.request.RoleCreationRequest;
import com.philong.identity_service.response.RoleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleCreationRequest request);
    RoleResponse toRoleResponse(Role role);
    List<RoleResponse> toRoleResponses(List<Role> roles);
}

// ignore chỉ định mapstruct bỏ qua k ánh xạ trường mục tiêu
// vì trong RoleCreationRequest có  Set<String> permissions là String
// còn Set<Permission> permissions là Permission nên mapstruct k thể map từ string sang permission nên t phải tự làm việc đó
