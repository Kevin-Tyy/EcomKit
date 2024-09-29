package org.retail.ecomkit.services;

import org.retail.ecomkit.domain.dto.TypeDTO;
import org.retail.ecomkit.domain.users.Client;
import org.retail.ecomkit.domain.users.Seller;
import org.retail.ecomkit.exceptions.ObjectNotFoundException;
import org.retail.ecomkit.repositories.ClientRepository;
import org.retail.ecomkit.repositories.SellerRepository;
import org.retail.ecomkit.services.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AuthService {

	private Random rand = new Random();

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private SellerRepository sellerRepository;

	@Autowired
	private BCryptPasswordEncoder pe;

	@Autowired
	private EmailService emailService;

	public void sendNewPassword(String email) {

		try {
			Client cli = clientRepository.findByEmail(email);
			String newPassword = newPassword();
			cli.setPassword(pe.encode(newPassword));
			clientRepository.save(cli);
			threadSendEmail(cli.getEmail(), newPassword);

		} catch (NullPointerException e) {
			Seller sel = sellerRepository.findByEmail(email);

			if (sel == null) {
				throw new ObjectNotFoundException();
			}

			String newPassword = newPassword();
			sel.setPassword(pe.encode(newPassword));
			sellerRepository.save(sel);
			threadSendEmail(sel.getEmail(), newPassword);

		}

	}

	private void threadSendEmail(String email, String newPassword) {
		Thread threadEmail = new Thread() {
			public void run() {
				emailService.sendNewPassword(email, newPassword);

			}
		};
		threadEmail.start();
	}

	public TypeDTO getTypeOfUser() {

		if (UserService.clientAuthenticated() != null) {
			return new TypeDTO("Client");
		} else if(UserService.sellerAuthenticated() != null) {
			return new TypeDTO("Seller");
		}else {
			throw new ObjectNotFoundException();
		}

	}

	private String newPassword() {
		char[] vet = new char[6];

		for (int i = 0; i < 6; i++) {
			vet[i] = randomChar();
		}

		return new String(vet);
	}

	private char randomChar() {
		int opt = rand.nextInt(3);

		switch (opt) {
		case 0:
			return (char) (rand.nextInt(10) + 48);
		case 1:
			return (char) (rand.nextInt(26) + 65);
		default:
			return (char) (rand.nextInt(26) + 97);
		}
	}

}
