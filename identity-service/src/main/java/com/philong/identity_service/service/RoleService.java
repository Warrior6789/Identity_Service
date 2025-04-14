package com.philong.identity_service.service;

import com.philong.identity_service.entity.Permission;
import com.philong.identity_service.entity.Role;
import com.philong.identity_service.mapper.RoleMapper;
import com.philong.identity_service.repository.PermissionRepository;
import com.philong.identity_service.repository.RoleRepository;
import com.philong.identity_service.request.RoleCreationRequest;
import com.philong.identity_service.response.RoleResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EnableMethodSecurity
public class RoleService implements IRoleService {

    RoleRepository roleRepository;
    RoleMapper roleMapper;
    PermissionRepository permissionRepository;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public RoleResponse create(RoleCreationRequest request) {
        Role role = roleMapper.toRole(request);

        var permission = permissionRepository.findAllById(request.getPermissions());
        permission.forEach(permission1 -> System.out.println(permission1.toString()));
        role.setPermissions(new HashSet<>(permission));
        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")

    public List<RoleResponse> getAll() {
        return roleMapper.toRoleResponses(roleRepository.findAll());
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")

    public void delete(String roleName) {
        roleRepository.deleteById(roleName);
    }
}
