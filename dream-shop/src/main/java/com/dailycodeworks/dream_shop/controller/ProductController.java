package com.dailycodeworks.dream_shop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodeworks.dream_shop.entity.Product;
import com.dailycodeworks.dream_shop.repository.ProductRepository;
import com.dailycodeworks.dream_shop.service.product.IProductService;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductRepository productRepository;
	@Autowired
	IProductService productService;

    ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
	
	public ResponseEntity<List<Product>> getAllProducts(){
		return new ResponseEntity(productService.getAllProducts(), HttpStatus.OK);
	}
}
