package com.philong.identity_service.service;

import com.philong.identity_service.entity.Permission;
import com.philong.identity_service.exception.AppException;
import com.philong.identity_service.exception.Error;
import com.philong.identity_service.mapper.PermissionMapper;
import com.philong.identity_service.repository.PermissionRepository;
import com.philong.identity_service.request.PermissionCreationRequest;
import com.philong.identity_service.request.PermissionUpdateRequest;
import com.philong.identity_service.response.PermissionResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@EnableMethodSecurity
public class PermissionService implements IPermissionService {

    PermissionMapper permissionMapper;
    PermissionRepository permissionRepository;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public PermissionResponse create(PermissionCreationRequest request) {
         Permission permission = permissionMapper.toPermission(request);
         permissionRepository.save(permission);
         return permissionMapper.toPermissionResponse(permission);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")

    public List<PermissionResponse> getAll() {
        return permissionMapper.toPermissionResponses(permissionRepository.findAll());
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")

    public void delete(String permissionName) {

        permissionRepository.deleteById(permissionName);
    }



    // vì bên role là sở hữu nên t k thể xóa permission
    // xóa role gắn với permission đó trc rồi mới xóa permission thì ok


}
