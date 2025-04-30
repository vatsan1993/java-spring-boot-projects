package com.dailycodeworks.dream_shop.dto;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.dailycodeworks.dream_shop.entity.CartItem;
import com.dailycodeworks.dream_shop.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
	
	private Long id;
    private BigDecimal totalAmount = BigDecimal.ZERO;
    private Set<CartItemDto> items = new HashSet<>();
}
