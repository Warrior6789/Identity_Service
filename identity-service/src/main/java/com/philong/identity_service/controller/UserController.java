package com.philong.identity_service.controller;

import com.philong.identity_service.entity.User;
import com.philong.identity_service.request.UserCreationRequest;
import com.philong.identity_service.request.UserUpdateRequest;
import com.philong.identity_service.response.ApiResponse;
import com.philong.identity_service.response.UserResponse;
import com.philong.identity_service.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class UserController {

    UserService userService;

    @GetMapping
    private ApiResponse<List<UserResponse>> getAllUser(){
        var authenthication = SecurityContextHolder.getContext().getAuthentication();
        log.info("username: {}", authenthication.getName());
        authenthication.getAuthorities().forEach(grantedAuthority ->
                log.info("grantedAuthority: {}", grantedAuthority.getAuthority()
                ));
        List<UserResponse> userList = userService.getAllUser();
        ApiResponse<List<UserResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userList);
        return apiResponse;
    }
    @GetMapping("/{id}")
    private ApiResponse<UserResponse> getUserById(@PathVariable String id){
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getUserById(id));
        return apiResponse;
    }
    @GetMapping("/myinfo")
    private ApiResponse<UserResponse> getMyInfo(){
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getMyInfo());
        return apiResponse;
    }
    @PostMapping
    private ApiResponse<UserResponse> addUser(@RequestBody UserCreationRequest request){
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.addUser(request));
        return apiResponse;
    }
    @PutMapping("/{id}")
    private ApiResponse<UserResponse> updateUser(@RequestBody UserUpdateRequest request, @PathVariable String id){
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.updateUser(request,id));
        return apiResponse;
    }

    @DeleteMapping("/{id}")
    private ApiResponse<Void> deleteUser(@PathVariable String id){
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        userService.deleteUser(id);
        return apiResponse;
    }
}

//SecurityContextHolder là điểm truy cập để lấy và thiết lập thông tin bảo mật trong springSecurity
// getContext() là cách truy cập thông tin về ng dùng đã xác thực và quyền của họ
// setContext() đc sd để lưu trữ thông tin
// Authentication trả về đối tượng Authentication
// chứa thông tin về quá trình xác thực
// principal: là đối tượng đại diện cho ng dùng đã xác thực
// credential: thông tin xác thực mà enduser cung cấp
// authorities: là collection đại diện cho role,permission
