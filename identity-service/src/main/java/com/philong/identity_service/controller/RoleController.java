package com.philong.identity_service.controller;

import com.philong.identity_service.request.PermissionCreationRequest;
import com.philong.identity_service.request.RoleCreationRequest;
import com.philong.identity_service.response.ApiResponse;
import com.philong.identity_service.response.PermissionResponse;
import com.philong.identity_service.response.RoleResponse;
import com.philong.identity_service.service.PermissionService;
import com.philong.identity_service.service.RoleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/role")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class RoleController {

    RoleService roleService;

    @GetMapping
    public ApiResponse<List<RoleResponse>> getAll() {
        ApiResponse<List<RoleResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(roleService.getAll()
        );
        return apiResponse;
    }

    @PostMapping
    public ApiResponse<RoleResponse> create(@RequestBody RoleCreationRequest request){
        ApiResponse<RoleResponse> apiResponse = new ApiResponse<>();
        RoleResponse roleResponse = roleService.create(request);
        apiResponse.setResult(roleResponse);
        return apiResponse;
    }

    @DeleteMapping("/{roleName}")
    public ApiResponse<Void> delete(@PathVariable String roleName){
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        roleService.delete(roleName);
        return apiResponse;
    }
}
