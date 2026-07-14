package com.commonlibrary.common_library.common.security;

import com.commonlibrary.common_library.common.mail.JavaMailCreator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;

@AutoConfiguration
@ConditionalOnProperty(name = "app.mail.enabled", havingValue = "true", matchIfMissing = false)
public class MailAutoConfiguration {

    @Bean
    public JavaMailCreator javaMailCreator(JavaMailSender javaMailSender) {
        return new JavaMailCreator(javaMailSender);
    }
}
