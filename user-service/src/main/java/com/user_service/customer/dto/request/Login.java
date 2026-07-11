package com.user_service.customer.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Login {
    @NotNull
    private String email;
    @Size(min = 4,max = 15, message = "password must not be less then 4 and more then 15 char")
    private String password;
}
