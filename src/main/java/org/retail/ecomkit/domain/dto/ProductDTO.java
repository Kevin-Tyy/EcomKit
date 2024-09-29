package org.retail.ecomkit.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.retail.ecomkit.domain.users.Client;
import org.retail.ecomkit.domain.users.Seller;

import java.io.Serializable;

public class ProductDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String name;
	private Double price;
	private String description;

	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Seller productOwner;
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Client buyerOfTheProduct;

	public ProductDTO() {

	}

	public ProductDTO(Integer id, String name, Double price, Seller productOwner,
			Client buyerOfTheProduct, String description) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.productOwner = productOwner;
		this.buyerOfTheProduct = buyerOfTheProduct;
		this.description = description;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Seller getProductOwner() {
		return productOwner;
	}

	public void setProductOwner(Seller productOwner) {
		this.productOwner = productOwner;
	}

	public Client getBuyerOfTheProduct() {
		return buyerOfTheProduct;
	}

	public void setBuyerOfTheProduct(Client buyerOfTheProduct) {
		this.buyerOfTheProduct = buyerOfTheProduct;
	}
	
	
	
	 
    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static boolean isSold(ProductDTO obj) {
    	
    	if(obj.getBuyerOfTheProduct() == null) {
    		return false;
    	}
    	return true;
    }



}
