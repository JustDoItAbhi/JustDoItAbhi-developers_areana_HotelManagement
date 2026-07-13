package com.commonlibrary.common_library.common.security;

import com.commonlibrary.common_library.common.mail.JavaMailCreator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;

@AutoConfiguration
public class MailAutoConfiguration {
    @Bean
    public JavaMailCreator javaMailCreator(JavaMailSender javaMailSender) {
        return new JavaMailCreator(javaMailSender);
    }
}
