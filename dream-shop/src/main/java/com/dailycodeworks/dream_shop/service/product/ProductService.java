package com.dailycodeworks.dream_shop.service.product;


import java.util.ArrayList;
import java.util.List;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dailycodeworks.dream_shop.dto.ImageDto;
import com.dailycodeworks.dream_shop.dto.ProductDto;
import com.dailycodeworks.dream_shop.entity.Category;
import com.dailycodeworks.dream_shop.entity.Image;
import com.dailycodeworks.dream_shop.entity.Product;
import com.dailycodeworks.dream_shop.exceptions.AlreadyExistsException;
import com.dailycodeworks.dream_shop.exceptions.ProductNotFoundException;
import com.dailycodeworks.dream_shop.repository.CategoryRepository;
import com.dailycodeworks.dream_shop.repository.ImageRepository;
import com.dailycodeworks.dream_shop.repository.ProductRepository;
import com.dailycodeworks.dream_shop.request.AddProductRequest;
import com.dailycodeworks.dream_shop.request.UpdateProductRequest;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductService implements IProductService {
	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final ImageRepository imageRepository;
	//Used to Map an entity to a model/dto
	private final ModelMapper modelMapper;
	
	@Override
	public Product addProduct(AddProductRequest request) {
		// check if category is found in the db
		// if yes, set it as new product category
		// if no, then save it as a new category
		// Then set it as new product's category.
		
		if(productExists(request.getName(), request.getBrand())) {
			throw new AlreadyExistsException(request.getBrand() + " " + request.getName()+" already exists");
		}
		
		Category category = Optional.ofNullable(categoryRepository.findByName(
				request.getCategory().getName()
		)).orElseGet(() ->{ 
			Category  newCategory = new Category(request.getCategory().getName());
			return categoryRepository.save(newCategory);
		});
		request.setCategory(category);
		Product theProduct = createProduct(request, category);
		return productRepository.save(theProduct);
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
	
	public boolean productExists(String name, String brand) {
		return productRepository.existsByNameAndBrand(name, brand);
	}
	
	@Override
	public Product getProductById(Long id) {
		// TODO Auto-generated method stub
		return productRepository.findById(id)
				.orElseThrow(()->new ProductNotFoundException("Product not found!") );
	}

	@Override
	public Product updateProduct(UpdateProductRequest request, Long productId) {
		// TODO Auto-generated method stub
		return productRepository.findById(productId)
				.map(existingProduct -> updateExistingProduct(existingProduct, request))
				.map(productRepository::save).orElseThrow(()-> new ProductNotFoundException("Product Not found!"));
	}
	
	private Product updateExistingProduct(Product existingProduct, UpdateProductRequest request) {
		existingProduct.setName(request.getName());
		existingProduct.setBrand(request.getBrand());
		existingProduct.setDescription(request.getDescription());
		existingProduct.setInventory(request.getInventory());
		existingProduct.setPrice(request.getPrice());
		
		Category category = categoryRepository.findByName(request.getCategory().getName());
		existingProduct.setCategory(category);
		return existingProduct;
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
		return productRepository.findByBrand(brand);
	}

	@Override
	public List<Product> getProductsByCategoryAndBrand(String categoryName, String brand) {
		// Note: WE are sending a string category
		return productRepository.findByCategoryNameAndBrand( categoryName,  brand);
	}

	@Override
	public List<Product> getProductsByName(String name) {
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
		return productRepository.countByBrandAndName(brand, name);
	}
	
	@Override
	public List<ProductDto> getConvertedProducts(List<Product> products) {
		return products.stream().map(this::convertToProductDto).toList();
	}

	
	@Override
	public ProductDto convertToProductDto(Product product) {
		ProductDto productDto = modelMapper.map(product, ProductDto.class);
		List<Image> images = imageRepository.findByProductId(product.getId());
		List<ImageDto> imagesDtos = images.stream()
				.map(image -> modelMapper.map(image, ImageDto.class))
				.toList();
		productDto.setImages(imagesDtos);
		return productDto;
	}

}
