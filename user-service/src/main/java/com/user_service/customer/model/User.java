package com.user_service.customer.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "USER")
public class User extends BaseModel {
private String name;
private String email;
private String password;
private String country;
private String state;
private String city;
private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private com.commonlibrary.common_library.common.enums.UserRole role;
}
