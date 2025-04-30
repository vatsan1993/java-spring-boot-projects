package com.dailycodeworks.dream_shop.dto;

import java.util.List;

import org.hibernate.annotations.NaturalId;

import com.dailycodeworks.dream_shop.entity.Cart;
import com.dailycodeworks.dream_shop.entity.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private CartDto cart;
	private List<OrderDto> order;
}
