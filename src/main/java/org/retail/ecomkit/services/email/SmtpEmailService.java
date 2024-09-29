package org.retail.ecomkit.services.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import jakarta.mail.internet.MimeMessage;

public class SmtpEmailService extends AbstractEmailService {

	@Autowired
	private MailSender mailSender;

	@Autowired
	private JavaMailSender javaMailSender;

	private static final Logger LOG = LoggerFactory.getLogger(SmtpEmailService.class);

	@Override
	public void sendEmail(SimpleMailMessage msg) {
		// TODO Auto-generated method stub
		LOG.info("Starting");
		mailSender.send(msg);
		LOG.info("Finished");
	}

	@Override
	public void sendEmailHtml(MimeMessage msg) {
		// TODO Auto-generated method stub
		LOG.info("Starting");
		javaMailSender.send(msg);
		LOG.info("Finished");

	}

}
