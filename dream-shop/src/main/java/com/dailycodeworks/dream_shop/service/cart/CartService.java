package com.dailycodeworks.dream_shop.service.cart;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;
import com.dailycodeworks.dream_shop.DreamShopApplication;
import com.dailycodeworks.dream_shop.entity.Cart;
import com.dailycodeworks.dream_shop.entity.CartItem;
import com.dailycodeworks.dream_shop.exceptions.ResourceNotFoundException;
import com.dailycodeworks.dream_shop.repository.CartItemRepository;
import com.dailycodeworks.dream_shop.repository.CartRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService{

    private final DreamShopApplication dreamShopApplication;
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final AtomicLong cartIdGenerator = new AtomicLong(0);

	

	@Override
	public Cart getCart(Long id) {
		// TODO Auto-generated method stub
		Cart cart = cartRepository.findById(id).
				orElseThrow(()-> new ResourceNotFoundException("Cart not found!"));
		BigDecimal totalAmount = cart.getTotalAmount();
		cart.setTotalAmount(totalAmount);
		
		return cartRepository.save(cart);
	}

	@Override
	public void clearCart(Long id) {
		// TODO Auto-generated method stub
		Cart cart = getCart(id);
		
		cartItemRepository.deleteAllByCartId(id);
		cart.getCartItems().clear();
		cartRepository.deleteById(id);
		
		
	}

	@Override
	public BigDecimal getTotalPrice(Long id) {
		Cart cart = getCart(id);
		
//		return cart.getCartItems().stream()
//				.map(CartItem :: getTotalPrice)
//				.reduce(BigDecimal.ZERO, BigDecimal::add);
	
		return cart.getTotalAmount();
	}

	
	// temporarily creating cart id . later on user entity will take care of cart.
	@Override
	public Long initializeNewCart() {
		Cart newCart = new Cart();
		Long newCartId = cartIdGenerator.incrementAndGet();
		newCart.setId(newCartId);
		
		return cartRepository.save(newCart).getId();
	}
}
