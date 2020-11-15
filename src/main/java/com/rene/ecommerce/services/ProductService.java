package com.rene.ecommerce.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.rene.ecommerce.domain.Product;
import com.rene.ecommerce.repositories.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private ClientService clientService;

	public Product findById(Integer id) {
		Optional<Product> obj = productRepo.findById(id);

		try {
			return obj.get();
		} catch (NoSuchElementException e) {
			throw new NoSuchElementException();
		}

	}

	@Transactional
	public Product insert(Product obj, Integer clientId) {
		obj.setId(null);
		obj.setOwnOfTheProduct(clientService.findById(clientId));
		return productRepo.save(obj);

	}

	@Transactional
	public Product update(Product obj) {
		return productRepo.save(obj);
	}

	public void delete(Integer id) {
		findById(id);

		try {
			productRepo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityViolationException("You can't delete this object");
		}
	}

	public List<Product> findAll() {
		return productRepo.findAll();
	}
	
    public Page<Product> findPage(Integer page, Integer line_per_page, String orderBy, String direction){
        PageRequest page_request = PageRequest.of(page, line_per_page, Direction.valueOf(direction), orderBy);
        
        return productRepo.findAll(page_request);
    }

}