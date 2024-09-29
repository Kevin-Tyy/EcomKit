package org.retail.ecomkit.services.email;

import org.retail.ecomkit.domain.Product;
import org.springframework.mail.SimpleMailMessage;

import jakarta.mail.internet.MimeMessage;

public interface EmailService {

	void sendConfirmationEmail(Product obj);

	void sendEmail(SimpleMailMessage msg);

	void sendConfirmationEmailHtml(Product obj);

	void sendEmailHtml(MimeMessage msg);

	void sendNewPassword(String email, String newPassword);
}
