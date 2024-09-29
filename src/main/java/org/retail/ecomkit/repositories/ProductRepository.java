package org.retail.ecomkit.repositories;

import org.retail.ecomkit.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	@Modifying
	@Query(value="delete from wishlist where product_id = :id",nativeQuery = true)
	void removeFromWishListWhenIsSold(@Param("id") Integer id);
	
	@Modifying
	@Query(value="delete from wishlist where product_id = :productId and client_id = :clientId",nativeQuery = true)
	void removeFromClientWishlist(@Param("productId") Integer productId, @Param("clientId") Integer clientId);
	
	
	List<Product> findByHasBeenSold(String hasBeenSold);
	

}
