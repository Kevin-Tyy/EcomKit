package org.retail.ecomkit.resources;

import org.retail.ecomkit.domain.dto.ranking.ClientRankingDTO;
import org.retail.ecomkit.domain.dto.updated.UpdatedClient;
import org.retail.ecomkit.domain.users.Client;
import org.retail.ecomkit.services.ClientService;
import org.retail.ecomkit.services.RankingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@Api(value = "Client resource")
@CrossOrigin
public class ClientResource {

	@Autowired
	private ClientService service;
	
	@Autowired
	private RankingService ranking;

	@GetMapping("/clients")
	@ApiOperation(value = "Return all clients")
	public ResponseEntity<List<Client>> findAll() {

		return ResponseEntity.ok().body(service.findAll());
	}

	@ApiOperation(value = "Return your own profile as Client")
	@GetMapping("/client")
	public ResponseEntity<Client> find() {

		Client obj = service.returnClientWithoutParsingTheId();
		return ResponseEntity.ok().body(obj);
	}
	
	@ApiOperation(value = "Create a client")
	@PostMapping("/create/client")
	public ResponseEntity<Client> insert(@RequestBody Client obj) {

		service.insert(obj);

		return ResponseEntity.ok().body(obj);
	}

	@PutMapping("/update/client")
	@ApiOperation(value = "Update a client ")
	public ResponseEntity<Client> update(@RequestBody UpdatedClient obj){

		Client cli =  service.update(obj);
		return ResponseEntity.ok().body(cli);
	}

	@DeleteMapping("/delete/client")
	@ApiOperation(value = "Delete a client")
	public ResponseEntity<Void> delete() {
		service.delete();

		return ResponseEntity.noContent().build();
	}
	

	@ApiOperation(value = "Return a list of clients who buys the most")
	@GetMapping("/clients/ranking")
	public ResponseEntity<List<ClientRankingDTO>> returnRankingClient() {
		
		
		return ResponseEntity.ok().body(ranking.returnRankingClient());
	}
}
