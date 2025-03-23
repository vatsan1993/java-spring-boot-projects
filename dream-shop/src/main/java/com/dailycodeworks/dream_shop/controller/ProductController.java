package com.dailycodeworks.dream_shop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodeworks.dream_shop.entity.Product;
import com.dailycodeworks.dream_shop.exceptions.ResourceNotFoundException;
import com.dailycodeworks.dream_shop.repository.ProductRepository;
import com.dailycodeworks.dream_shop.request.AddProductRequest;
import com.dailycodeworks.dream_shop.request.UpdateProductRequest;
import com.dailycodeworks.dream_shop.response.ApiResponse;
import com.dailycodeworks.dream_shop.service.product.IProductService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {

	@Autowired
	IProductService productService;
	
	@GetMapping("/")
	public ResponseEntity<ApiResponse> getAllProducts(){
		try {
			List<Product> products = productService.getAllProducts();
			return  ResponseEntity.ok(new ApiResponse("Found All", products ));
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse(e.getMessage(), null));
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id){
		try {
			Product product = productService.getProductById(id);
			return  ResponseEntity.ok(new ApiResponse("Found product", product ));
		}catch(ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ApiResponse(e.getMessage(), null));
		}
	}
	
	@PostMapping("/")
	public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest request){
		try {
			
			Product product = productService.addProduct(request);
			
			return  ResponseEntity.ok(new ApiResponse("New Product Added", product ));
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse(e.getMessage(), null));
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse> updateProduct(@RequestBody UpdateProductRequest request, @PathVariable Long productId){
		try {
			
			Product product = productService.updateProduct(request, productId);
			return  ResponseEntity.ok(new ApiResponse("New Product Added", product ));
		}catch(ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ApiResponse(e.getMessage(), null));
		}
	}
	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteProduct( @PathVariable Long productId){
		try {
			
			productService.deleteProduct( productId);
			return  ResponseEntity.ok(new ApiResponse("Delete Product success!", productId));
		}catch(ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ApiResponse(e.getMessage(), null));
		}
	}
	
	@GetMapping("/by-brand-and-name/{brandName}/{productName}")
	public ResponseEntity<ApiResponse> getProductByBrandAndName( @PathVariable String brandName, @PathVariable String productName){
		try {
			
			List<Product> products= productService.getProductByBrandAndName( brandName, productName);
			if(products.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No products found", null));
			}
			return  ResponseEntity.ok(new ApiResponse("Found Products", products));
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse(e.getMessage(), null));
		}
	}
	
	@GetMapping("/by-category-and-brand/{categoryName}/{brandName}")
	public ResponseEntity<ApiResponse> getProductByCategoryAndBrand( @PathVariable String categoryName, @PathVariable String brandName){
		try {
			
			List<Product> products= productService.getProductsByCategoryAndBrand( categoryName, brandName);
			if(products.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No products found", null));
			}
			return  ResponseEntity.ok(new ApiResponse("Found Products", products));
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse(e.getMessage(), null));
		}
	}
	
	
	@GetMapping("/by-name/{productName}")
	public ResponseEntity<ApiResponse> getProductsByName( @PathVariable String productName){
		try {
			
			List<Product> products= productService.getProductsByName( productName);
			if(products.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No products found", null));
			}
			return  ResponseEntity.ok(new ApiResponse("Found Products", products));
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse(e.getMessage(), null));
		}
	}
	
	
	@GetMapping("/by-category/{categoryName}")
	public ResponseEntity<ApiResponse> getProductsByCategory( @PathVariable String categoryName){
		try {
			
			List<Product> products= productService.getProductsByCategory( categoryName);
			if(products.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No products found", null));
			}
			return  ResponseEntity.ok(new ApiResponse("Found Products", products));
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse(e.getMessage(), null));
		}
	}

	
	@GetMapping("/by-brand/{categoryName}")
	public ResponseEntity<ApiResponse> getProductsByBrand( @PathVariable String brandName){
		try {
			
			List<Product> products= productService.getProductsByBrand( brandName);
			if(products.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No products found", null));
			}
			return  ResponseEntity.ok(new ApiResponse("Found Products", products));
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse(e.getMessage(), null));
		}
	}


}
