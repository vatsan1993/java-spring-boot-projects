package com.dailycodeworks.dream_shop.service.product;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dailycodeworks.dream_shop.entity.Category;
import com.dailycodeworks.dream_shop.entity.Product;
import com.dailycodeworks.dream_shop.exceptions.ProductNotFoundException;
import com.dailycodeworks.dream_shop.repository.CategoryRepository;
import com.dailycodeworks.dream_shop.repository.ProductRepository;
import com.dailycodeworks.dream_shop.request.AddProductRequest;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductService implements IProductService {
	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	
	@Override
	public Product addProduct(AddProductRequest request) {
		// check if category is found in the db
		// if yes, set it as new product category
		// if no, then save it as a new category
		// Then set it as new product's category.
		Category category = Optional.ofNullable(categoryRepository.findByName(
				request.getCategory().getName()
		)).orElse(null);
		return null;
	}

//	helper method to add product
	public Product createProduct(AddProductRequest request,  Category category) {
		return new Product(
				request.getName(),
				request.getBrand(),
				request.getPrice(),
				request.getInventory(),
				request.getDescription(),
				category
		);
	}
	
	
	
	@Override
	public Product getProductById(Long id) {
		// TODO Auto-generated method stub
		return productRepository.findById(id)
				.orElseThrow(()->new ProductNotFoundException("Product not found!") );
	}

	@Override
	public void updateProduct(Product product, Long productId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteProduct(Long id) {
//		productRepository.findById(id).ifPresent(productRepository::delete);
		productRepository.findById(id).ifPresentOrElse(productRepository::delete, ()->{
			throw new ProductNotFoundException("product Not found!");
		});
		
	}

	@Override
	public List<Product> getAllProducts() {
		// TODO Auto-generated method stub
		return productRepository.findAll();
	}

	@Override
	public List<Product> getProductsByCategory(String categoryName) {
		// TODO Auto-generated method stub
		return productRepository.findByCategoryName(categoryName);
	}

	@Override
	public List<Product> getProductsByBrand(String brand) {
		// TODO Auto-generated method stub
		return productRepository.findByBrandName(brand);
	}

	@Override
	public List<Product> getProductsByCategoryAndBrand(String categoryName, String brand) {
		// Note: WE are sending a string category
		return productRepository.findByCategoryNameAndBrand( categoryName,  brand);
	}

	@Override
	public List<Product> getProductByName(String name) {
		// TODO Auto-generated method stub
		return productRepository.findByName(name);
	}

	@Override
	public List<Product> getProductByBrandAndName(String brand, String name) {
		// TODO Auto-generated method stub
		return productRepository.findByBrandAndName(brand, name);
	}

	@Override
	public Long countProductsByBrandAndName(String brand, String name) {
		// TODO Auto-generated method stub
		return productRepository.countProductsByBrandAndName(brand, name);
	}

}
