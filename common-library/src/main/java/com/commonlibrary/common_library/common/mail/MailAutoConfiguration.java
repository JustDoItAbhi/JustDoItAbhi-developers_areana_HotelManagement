package com.commonlibrary.common_library.common.mail;

import com.commonlibrary.common_library.common.mail.JavaMailCreator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;

@AutoConfiguration
@AutoConfigureAfter(MailSenderAutoConfiguration.class)
@ConditionalOnProperty(
        name = "app.mail.enabled",
        havingValue = "true"
)
public class MailAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public JavaMailCreator javaMailCreator(JavaMailSender javaMailSender) {
        return new JavaMailCreator(javaMailSender);
    }
}