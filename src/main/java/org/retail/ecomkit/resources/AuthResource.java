package org.retail.ecomkit.resources;

import org.retail.ecomkit.domain.dto.EmailDTO;
import org.retail.ecomkit.domain.dto.TypeDTO;
import org.retail.ecomkit.services.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "Auth service")
@CrossOrigin
@RequestMapping
public class AuthResource {

	@Autowired
	private AuthService service;

	@PostMapping("/forgot")
	@ApiOperation(value = "send a new password to email")
	public ResponseEntity<Void> sendNewPassowrd(@RequestBody EmailDTO email) {

		service.sendNewPassword(email.getEmail());
		
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/user")
	@ApiOperation(value = "return if the user is a client or a seller")
	public ResponseEntity<TypeDTO> getTypeOfUser() {

		
		return ResponseEntity.ok().body(service.getTypeOfUser());
	}
	


}
