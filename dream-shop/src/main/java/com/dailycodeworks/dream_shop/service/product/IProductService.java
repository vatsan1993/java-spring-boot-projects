package com.dailycodeworks.dream_shop.service.product;

import java.util.List;

import com.dailycodeworks.dream_shop.entity.Product;
import com.dailycodeworks.dream_shop.request.AddProductRequest;

public interface IProductService {
	public Product addProduct(AddProductRequest request);
	public Product getProductById(Long id);
	public void updateProduct(Product product, Long productId);
	public void deleteProduct(Long id);
	public List<Product> getAllProducts();
	public List<Product> getProductsByCategory(String category);
	public List<Product> getProductsByBrand(String brand);
	public List<Product> getProductsByCategoryAndBrand(String category, String brand);
	public List<Product> getProductByName( String name);
	public List<Product> getProductByBrandAndName(String brand, String name);
	public Long countProductsByBrandAndName(String brand, String name);
}
