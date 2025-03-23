package com.dailycodeworks.dream_shop.dto;

import java.math.BigDecimal;
import java.util.List;

import com.dailycodeworks.dream_shop.entity.Category;
import com.dailycodeworks.dream_shop.entity.Image;

import lombok.Data;

@Data
public class ProductDto {
	private Long id;
	private String name;
	private String brand;
	private BigDecimal price;
	private Integer inventory;
	private String description;
	private Category category;
	private List<ImageDto> images;
	
	
}
