package com.philong.identity_service.controller;

import com.philong.identity_service.request.PermissionCreationRequest;
import com.philong.identity_service.request.PermissionUpdateRequest;
import com.philong.identity_service.response.ApiResponse;
import com.philong.identity_service.response.PermissionResponse;
import com.philong.identity_service.service.PermissionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/permission")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class PermissionController {

    PermissionService permissionService;

    @GetMapping
    public ApiResponse<List<PermissionResponse>> getAllPermissions() {
        ApiResponse<List<PermissionResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(permissionService.getAll()
        );
        return apiResponse;
    }

    @PostMapping
    public ApiResponse<PermissionResponse> create(@RequestBody PermissionCreationRequest request){
        ApiResponse<PermissionResponse> apiResponse = new ApiResponse<>();
        PermissionResponse permissionResponse = permissionService.create(request);
        apiResponse.setResult(permissionResponse);
        return apiResponse;
    }



    @DeleteMapping("/{permissionName}")
    public ApiResponse<Void> delete(@PathVariable String permissionName){
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        permissionService.delete(permissionName);
        return apiResponse;
    }
}
