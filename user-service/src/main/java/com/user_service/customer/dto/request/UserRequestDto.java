package com.user_service.customer.dto.request;

import com.commonlibrary.common_library.common.enums.UserRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestDto {
    @NotNull
    private String name;
    @Email
    @NotNull
    private String email;
    @Size(min = 4,max = 15, message = "password must not be less then 4 and more then 15 char")
    private String password;
    private String country;
    private String state;
    private String city;
    @NotNull
    private String phoneNumber;
    private String role;

}
