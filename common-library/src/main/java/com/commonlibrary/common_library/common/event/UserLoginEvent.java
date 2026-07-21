package com.commonlibrary.common_library.common.event;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserLoginEvent {
private String userName;
private String userEmail;
private LocalDateTime loginTime;
private String userRole;
}

