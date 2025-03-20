package com.dailycodeworks.dream_shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dailycodeworks.dream_shop.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	
	List<Product> findByCategoryName(String category);
	List<Product> findByBrandName(String brand);
	List<Product> findByCategoryNameAndBrand(String category, String brand);
	List<Product> findByName(String name);
	List<Product> findByBrandAndName(String beand, String name);
	Long countProductsByBrandAndName(String brand, String name);

}
