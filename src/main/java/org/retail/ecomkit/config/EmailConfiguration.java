package org.retail.ecomkit.config;

import org.retail.ecomkit.services.email.EmailService;
import org.retail.ecomkit.services.email.SmtpEmailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailConfiguration {

	@Bean
	public EmailService emailService() {
		return new SmtpEmailService();
	}
}
