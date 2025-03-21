package com.dailycodeworks.dream_shop.service.category;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dailycodeworks.dream_shop.entity.Category;
import com.dailycodeworks.dream_shop.exceptions.AlreadyExistsException;
import com.dailycodeworks.dream_shop.exceptions.ResourceNotFoundException;
import com.dailycodeworks.dream_shop.repository.CategoryRepository;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{
	private final CategoryRepository categoryRepository;
	
	@Override
	public Category getCategoryById(Long id) {
		// TODO Auto-generated method stub
		return categoryRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Category Not foundd!"));
	}

	@Override
	public Category getCategoryByName(String name) {
		// TODO Auto-generated method stub
		return categoryRepository.findByName(name);
	}

	@Override
	public List<Category> getAllCategories() {
		// TODO Auto-generated method stub
		return categoryRepository.findAll();
	}

	@Override
	public Category addCategory(Category category) {
		// TODO Auto-generated method stub
		return Optional.of(category)
				.filter(c -> !categoryRepository.existsByName(c.getName()))
				.map(categoryRepository::save).orElseThrow(
						() ->{
							throw new AlreadyExistsException(category.getName()+" already Exists");
						}
				);
	}

	@Override
	public Category updateCategory(Long categoryId, Category category) {
		// TODO Auto-generated method stub
		return Optional.ofNullable(getCategoryById(categoryId))
				.map(existingCategory -> {
					existingCategory.setName(category.getName());
					return categoryRepository.save(existingCategory);
				}).orElseThrow(() ->{
					throw new  ResourceNotFoundException("Category Not found");
				});
	}

	@Override
	public void deleteCategory(Long id) {
		// TODO Auto-generated method stub
		categoryRepository.findById(id).ifPresentOrElse(
				categoryRepository::delete, ()-> {
					throw new ResourceNotFoundException("Category Not found!");  
				}
		);
	}	

}
