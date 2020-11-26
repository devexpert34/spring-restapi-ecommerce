package com.rene.ecommerce.services;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rene.ecommerce.domain.Product;
import com.rene.ecommerce.domain.users.Client;
import com.rene.ecommerce.exceptions.ObjectNotFoundException;
import com.rene.ecommerce.exceptions.ProductHasAlreadyBeenSold;
import com.rene.ecommerce.repositories.ProductRepository;
import com.rene.ecommerce.services.email.EmailService;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepo;

	@Autowired
	private SellerService sellerService;

	@Autowired
	private ClientService clientService;

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private EmailService emailService;

	public Product findById(Integer id) {
		Optional<Product> obj = productRepo.findById(id);

		try {
			return obj.get();
		} catch (NoSuchElementException e) {
			throw new ObjectNotFoundException();
		}

	}

	@Transactional
	public Product insert(Product obj, Integer sellerId, Integer categoryId) {
		obj.setId(null);
		obj.setProductOwner(sellerService.findById(sellerId));
		obj.setCategory(categoryService.findById(categoryId));
		return productRepo.save(obj);

	}

	@Transactional
	public Product update(Product obj, Integer sellerId, Integer categoryId)  {
		if (Product.isSold(findById(obj.getId()))) {
			throw new ProductHasAlreadyBeenSold();
		}

		obj.setProductOwner(sellerService.findById(sellerId));
		obj.setCategory(categoryService.findById(categoryId));
		return productRepo.save(obj);

	}

	public void delete(Integer id)  {

		if (Product.isSold(findById(id))) {
			throw new ProductHasAlreadyBeenSold();
		}
		productRepo.deleteById(id);

	}

	public List<Product> findAll() {
		return productRepo.findAll();
	}

	@Transactional
	public Product buyProduct(Integer productId, Integer clientId)  {

		if (Product.isSold(findById(productId))) {
			throw new ProductHasAlreadyBeenSold();
		}
		Product boughtProduct = findById(productId);
		Client buyer = clientService.findById(clientId);

		buyer.setBoughtProducts(Arrays.asList(boughtProduct));
		boughtProduct.setBuyerOfTheProduct(buyer);

		emailService.sendConfirmationEmail(boughtProduct);
		return productRepo.save(boughtProduct);
	}

}
