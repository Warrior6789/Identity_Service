package com.philong.identity_service.config;

import com.philong.identity_service.entity.Role;
import com.philong.identity_service.exception.Error;
import com.philong.identity_service.entity.User;
import com.philong.identity_service.exception.AppException;
import com.philong.identity_service.repository.RoleRepository;
import com.philong.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;


    @Bean
    ApplicationRunner runner(UserRepository userRepository) {
        return args -> {
            Role adminRole = roleRepository.findById("ADMIN").orElseGet(() -> {
                Role newAdminRole = new Role();
                newAdminRole.setName("ADMIN");
                newAdminRole.setDescription("Admin Role");
                return roleRepository.save(newAdminRole);
            });

            if(userRepository.findByUsername("admin").isEmpty()){
                var  roles = new HashSet<Role>();
                roles.add(adminRole);
                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
//                        .role(roles)
                        .build();
                userRepository.save(user);
                log.warn("admin user has been created with default password: admin, please change");
            }

        };
    }
}
//