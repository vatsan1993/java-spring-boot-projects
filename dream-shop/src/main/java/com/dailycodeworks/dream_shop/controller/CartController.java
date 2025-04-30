package com.dailycodeworks.dream_shop.controller;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodeworks.dream_shop.entity.Cart;
import com.dailycodeworks.dream_shop.exceptions.ResourceNotFoundException;
import com.dailycodeworks.dream_shop.response.ApiResponse;
import com.dailycodeworks.dream_shop.service.cart.ICartService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/carts")
public class CartController {
	private final ICartService cartService;	
	
	@GetMapping("/{cartId}")
	public ResponseEntity<ApiResponse> getCart(@PathVariable Long cartId){
		try {
			Cart cart = cartService.getCart(cartId);
			return ResponseEntity.ok(new ApiResponse("success", cart));
		}catch(ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ApiResponse(e.getMessage(), null));
		}
	}
	
	
	@GetMapping("/{cartId}/clear")
	public ResponseEntity<ApiResponse> clearCart(@PathVariable Long cartId){
		try {
			cartService.clearCart(cartId);
			return ResponseEntity.ok(new ApiResponse("success", null));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ApiResponse(e.getMessage(), null));
		}
	}
	@GetMapping("/{cartId}/total-price")
	public ResponseEntity<ApiResponse> totalPrice(@PathVariable Long cartId){
		try {
			BigDecimal totalPrice =cartService.getTotalPrice(cartId);
			return ResponseEntity.ok(new ApiResponse("Total Price", totalPrice));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ApiResponse(e.getMessage(), null));
		}
	}
}
