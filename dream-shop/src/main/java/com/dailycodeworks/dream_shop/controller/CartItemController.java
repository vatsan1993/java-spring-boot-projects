package com.dailycodeworks.dream_shop.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodeworks.dream_shop.entity.Cart;
import com.dailycodeworks.dream_shop.entity.User;
import com.dailycodeworks.dream_shop.exceptions.ResourceNotFoundException;
import com.dailycodeworks.dream_shop.response.ApiResponse;
import com.dailycodeworks.dream_shop.service.cart.CartService;
import com.dailycodeworks.dream_shop.service.cart.ICartService;
import com.dailycodeworks.dream_shop.service.cartItem.CartItemService;
import com.dailycodeworks.dream_shop.service.cartItem.ICartItemService;
import com.dailycodeworks.dream_shop.service.user.IUserService;

import io.jsonwebtoken.JwtException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {
	  private final ICartItemService cartItemService;
	    private final ICartService cartService;
	    private final IUserService userService;


	    @PostMapping("/item/add")
	    public ResponseEntity<ApiResponse> addItemToCart(
	                                                     @RequestParam Long productId,
	                                                     @RequestParam Integer quantity) {
	        try {
	        	// Will be changed later
//	            User user = userService.getUserById(4L);
	        	// instead of getting a user directly, we will create a different method
	            User user = userService.getAuthenticatedUser();
	            Cart cart = cartService.initializeNewCart(user);
	        	
	        	
	            cartItemService.addItemToCart(cart.getId(), productId, quantity);
	            return ResponseEntity.ok(new ApiResponse("Add Item Success", null));
	        } catch (ResourceNotFoundException e) {
	        	System.out.println("Resource not found Error");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
	        }catch(JwtException e) {
	        	System.out.println("JWT Exception");
	        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
	        	
	        }
	    }

	    @DeleteMapping("/cart/{cartId}/item/{itemId}/remove")
	    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long itemId) {
	        try {
	            cartItemService.removeItemFromCart(cartId, itemId);
	            return ResponseEntity.ok(new ApiResponse("Remove Item Success", null));
	        } catch (ResourceNotFoundException e) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
	        }
	    }

	    @PutMapping("/cart/{cartId}/item/{itemId}/update")
	    public  ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId,
	                                                           @PathVariable Long itemId,
	                                                           @RequestParam Integer quantity) {
	        try {
	            cartItemService.updateItemQuantity(cartId, itemId, quantity);
	            return ResponseEntity.ok(new ApiResponse("Update Item Success", null));
	        } catch (ResourceNotFoundException e) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
	        }

	    }
}
