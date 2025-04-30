package com.dailycodeworks.dream_shop.dto;

import java.math.BigDecimal;

import com.dailycodeworks.dream_shop.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class CartItemDto {
	private Long id;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private ProductDto product;

}
