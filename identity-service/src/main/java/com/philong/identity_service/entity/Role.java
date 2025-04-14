package com.philong.identity_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Role {
    @Id
    String name;
    String description;

    @ManyToMany
    Set<Permission> permissions;

}

// Owning side chịu trách nhiệm chính trong việc cập nhật các thay đổi cho mối quan hệ
// trong db
// inverse side k chịu trách nhiệm crud trong mối quan hệ  nó phản ánh trạng thía hiện tại
// của mối quan hệ dựa trên thông tin đc quản lý