package com.dailycodeworks.dream_shop.service.product;

import java.util.List;

import com.dailycodeworks.dream_shop.entity.Product;
import com.dailycodeworks.dream_shop.request.AddProductRequest;
import com.dailycodeworks.dream_shop.request.UpdateProductRequest;

public interface IProductService {
	public Product addProduct(AddProductRequest product);
	public Product getProductById(Long id);
	
	public void deleteProduct(Long id);
	public List<Product> getAllProducts();
	public List<Product> getProductsByCategory(String category);
	public List<Product> getProductsByBrand(String brand);
	public List<Product> getProductsByCategoryAndBrand(String category, String brand);
	public List<Product> getProductsByName( String name);
	public List<Product> getProductByBrandAndName(String brand, String name);
	public Long countProductsByBrandAndName(String brand, String name);
	Product updateProduct(UpdateProductRequest product, Long productId);

}
