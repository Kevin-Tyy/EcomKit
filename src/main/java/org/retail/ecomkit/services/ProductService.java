package org.retail.ecomkit.services;

import org.retail.ecomkit.domain.Order;
import org.retail.ecomkit.domain.Product;
import org.retail.ecomkit.domain.dto.updated.UpdatedProduct;
import org.retail.ecomkit.domain.users.Client;
import org.retail.ecomkit.domain.users.Seller;
import org.retail.ecomkit.exceptions.AuthorizationException;
import org.retail.ecomkit.exceptions.ObjectNotFoundException;
import org.retail.ecomkit.exceptions.ProductHasAlreadyBeenSold;
import org.retail.ecomkit.repositories.OrderRepository;
import org.retail.ecomkit.repositories.ProductRepository;
import org.retail.ecomkit.security.ClientSS;
import org.retail.ecomkit.security.SellerSS;
import org.retail.ecomkit.services.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepo;

	@Autowired
	private OrderRepository orderRepo;

	@Autowired
	private SellerService sellerService;

	@Autowired
	private ClientService clientService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private WishlistService wishlistService;

	public Product findById(Integer id) {
		Optional<Product> obj = productRepo.findById(id);

		try {
			return obj.get();
		} catch (NoSuchElementException e) {
			throw new ObjectNotFoundException();
		}

	}

	@Transactional
	public Product insert(Product obj) {

		SellerSS user = UserService.sellerAuthenticated();

		obj.setId(null);
		obj.setProductOwner(sellerService.findById(user.getId()));
		obj.setHasBeenSold("Unsold");
		return productRepo.save(obj);

	}

	// verify if its product owner
	@Transactional
	public Product update(UpdatedProduct obj, Integer productId) {
		SellerSS user = UserService.sellerAuthenticated();
		Seller seller = sellerService.findById(user.getId());
		Product product = findById(productId);

		if (!product.getProductOwner().equals(seller)) {
			throw new AuthorizationException("You're not owner of this product");
		}
		if (Product.isSold(findById(product.getId()))) {
			throw new ProductHasAlreadyBeenSold();
		}
		product.setName(obj.getName());
		product.setDescription(obj.getDescription());
		product.setPrice(obj.getPrice());

		return productRepo.save(product);

	}

	public void delete(Integer id) {
		SellerSS user = UserService.sellerAuthenticated();
		Seller seller = sellerService.findById(user.getId());
		Product obj = findById(id);

		if (!obj.getProductOwner().equals(seller)) {
			throw new AuthorizationException("You're not owner of this product");
		}
		if (Product.isSold(findById(id))) {
			throw new ProductHasAlreadyBeenSold();
		}
		productRepo.deleteById(id);

	}

	public List<Product> findAll() {

		return productRepo.findByHasBeenSold("Unsold");
	}

	public List<Product> findOwnProducts() {

		SellerSS user = UserService.sellerAuthenticated();
		Seller seller = sellerService.findById(user.getId());

		return seller.getOwnProducts();
	}

	@Transactional
	public Product buyProduct(Integer productId) {

		if (Product.isSold(findById(productId))) {
			throw new ProductHasAlreadyBeenSold();
		}

		ClientSS user = UserService.clientAuthenticated();
		Client buyer = clientService.findById(user.getId());
		Product boughtProduct = findById(productId);

		buyer.setBoughtProducts(Arrays.asList(boughtProduct));
		boughtProduct.setBuyerOfTheProduct(buyer);
		boughtProduct.setHasBeenSold("Sold");

		// method to add: number of buys and sells | money spent and sold
		addNumberOfBuysAndSellsAndMoneySoldAndSpent(boughtProduct, buyer);

		// remove product from wishList
		threadRemoveProductFromWishlist(boughtProduct);

		// thread to send email
		threadSendEmail(boughtProduct);

		// Save order entity
		threadSaveOrder(boughtProduct);

		return productRepo.save(boughtProduct);

	}

	private void threadSendEmail(Product boughtProduct) {
		Thread threadEmail = new Thread() {
			public void run() {
				emailService.sendConfirmationEmailHtml(boughtProduct);

			}
		};
		threadEmail.start();
	}

	private void addNumberOfBuysAndSellsAndMoneySoldAndSpent(Product boughtProduct, Client buyer) {
		Seller sel = boughtProduct.getProductOwner();
		Double price = boughtProduct.getPrice();

		buyer.addNumberOfBuys();
		sel.addNumberOfSells();

		buyer.addSpentMoneyWhenClientBuyAProduct(price);
		sel.addSoldMoneyWhenSellerSellAProduct(price);

	}

	private void threadRemoveProductFromWishlist(Product boughtProduct) {
		Thread threadRemove = new Thread() {
			public void run() {
				wishlistService.removeProductFromWishlistWhenIsSold(boughtProduct.getId());
			}
		};
		threadRemove.start();
	}

	private void threadSaveOrder(Product product) {

		Thread threadSaveOrder = new Thread() {
			public void run() {

				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				Date date = new Date(System.currentTimeMillis());
				String instant = sdf.format(date);

				Order order = new Order(null, instant, product);

				orderRepo.save(order);
			}
		};
		threadSaveOrder.start();

	}

}
