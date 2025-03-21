package com.dailycodeworks.dream_shop.service.category;

import java.util.List;

import com.dailycodeworks.dream_shop.entity.Category;


public interface ICategoryService {
	
	public Category getCategoryById(Long id);
	public Category getCategoryByName(String name);
	List<Category> getAllCategories();
	Category addCategory(Category category);
	void deleteCategory(Long id);
	Category updateCategory(Long categoryId, Category category);

}
