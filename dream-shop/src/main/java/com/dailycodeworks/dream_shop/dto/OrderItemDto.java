package com.dailycodeworks.dream_shop.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderItemDto {
	private Long productId;
	private String productName;
	private String productBrand;
	private Integer quality;
	private BigDecimal price;
	
}
