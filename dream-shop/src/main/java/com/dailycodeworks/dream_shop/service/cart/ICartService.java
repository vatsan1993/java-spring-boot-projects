package com.dailycodeworks.dream_shop.service.cart;

import java.math.BigDecimal;

import com.dailycodeworks.dream_shop.entity.Cart;
import com.dailycodeworks.dream_shop.entity.User;

public interface ICartService {
	Cart getCart(Long id);
	void clearCart(Long id);
	BigDecimal getTotalPrice(Long id);
	Cart getCartByUserId(Long userId);
	Cart initializeNewCart(User user);
}
