package org.retail.ecomkit.resources;

import org.retail.ecomkit.domain.Product;
import org.retail.ecomkit.domain.dto.ProductDTO;
import org.retail.ecomkit.domain.dto.updated.UpdatedProduct;
import org.retail.ecomkit.services.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@Api(value = "Product resource")
@CrossOrigin
public class ProductResource {

	@Autowired
	private ProductService service;

	@GetMapping("/product/{id}")
	@ApiOperation(value = "Return a product by id")
	public ResponseEntity<ProductDTO> findById(@PathVariable Integer id) {

		Product obj = service.findById(id);
		ProductDTO dto = new ProductDTO(obj.getId(), obj.getName(), obj.getPrice(), obj.getProductOwner(),
				obj.getBuyerOfTheProduct(), obj.getDescription());
		return ResponseEntity.ok().body(dto);
	}

	@GetMapping("/products")
	@ApiOperation(value = "Return all products")

	public ResponseEntity<List<Product>> findAll() {

		List<Product> products = service.findAll();
		return ResponseEntity.ok().body(products);
	}
	

	@GetMapping("/ownproducts")
	@ApiOperation(value = "Return own products")

	public ResponseEntity<List<Product>> findOwnProducts() {

		List<Product> products = service.findOwnProducts();
		return ResponseEntity.ok().body(products);
	}


	@ApiOperation(value = "Create a product")
	@PostMapping("/product")
	public ResponseEntity<Product> insert(@RequestBody ProductDTO obj) {

		Product product = new Product(null, obj.getName(), obj.getPrice(), null, obj.getDescription());

		service.insert(product);

		return ResponseEntity.ok().body(product);
	}

	@ApiOperation(value = "Update a product")
	@PutMapping("/product/{productId}")
	public ResponseEntity<Product> update(@RequestBody UpdatedProduct obj,
			@PathVariable Integer productId) {

		Product product = service.update(obj, productId);

		return ResponseEntity.ok().body(product);
	}

	@ApiOperation(value = "Buy a product and send a confirmation email to client and to the seller")
	@PutMapping("buy/{productId}")
	public ResponseEntity<Void> buyProduct(@PathVariable Integer productId) {

		service.buyProduct(productId);
		return ResponseEntity.ok().build();

	}

	@ApiOperation(value = "Delete a product")
	@DeleteMapping("product/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {

		service.delete(id);
		return ResponseEntity.ok().build();

	}

}
