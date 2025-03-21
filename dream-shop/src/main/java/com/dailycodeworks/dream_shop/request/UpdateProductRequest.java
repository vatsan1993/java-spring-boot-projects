package com.dailycodeworks.dream_shop.request;

import java.math.BigDecimal;

import com.dailycodeworks.dream_shop.entity.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


// As we add a product to the database and the images might not 
// be available at the beginning, we have this class which will be intermediate stage
// for adding a product. we should not directly work with the Entity in this case.


// The data will have getters, setters, equals, and hasCode
// It should not be used on the Entity class.
// This class is not an entity. so we can use the @Data
@Getter
@Setter
@AllArgsConstructor
public class UpdateProductRequest {
	private Long id;
	private String name;
	private String brand;
	private BigDecimal price;
	private Integer inventory;
	private String description;
	private Category category;
	
}
