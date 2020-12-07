package com.rene.ecommerce.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rene.ecommerce.domain.dto.ranking.SellerRankingDTO;
import com.rene.ecommerce.domain.users.Seller;
import com.rene.ecommerce.services.RankingService;
import com.rene.ecommerce.services.SellerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping
@Api(value = "Client resource")
@CrossOrigin(origins = "*")
public class SellerResource {

	@Autowired
	private SellerService service;

	@Autowired
	private RankingService ranking;
	
	@GetMapping("/seller/{id}")
	@ApiOperation(value = "Return a seller by id")
	public ResponseEntity<Seller> findById(@PathVariable Integer id) {

		Seller obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}
	
	@GetMapping("/sellers")
	@ApiOperation(value = "Return all seller")
	public ResponseEntity<List<Seller>> findAll() {

		return ResponseEntity.ok().body(service.findAll());
	}


	@ApiOperation(value = "Create a seller")
	@PostMapping("/create/seller")
	public ResponseEntity<Seller> insert(@RequestBody Seller obj) {

		service.insert(obj);
	
		return ResponseEntity.ok().body(obj);
	}
	
	@ApiOperation(value = "Update a seller")
	@PutMapping("/seller/update")
	public ResponseEntity<Void> update(@RequestBody Seller obj){
	
	service.update(obj);
	return ResponseEntity.noContent().build();
	}
	
	@ApiOperation(value = "Delete a seller")
	@DeleteMapping("/seller/delete")
	public ResponseEntity<Void> delete(){
		service.delete();
		
		return ResponseEntity.noContent().build();
	}
	
	@ApiOperation(value = "Return a list of sellers who sells the most")
	@GetMapping("/sellers/ranking")
	public ResponseEntity<List<SellerRankingDTO>> returnRankingSeller() {

		
		return ResponseEntity.ok().body(ranking.returnRankingSeller());
	}

}
