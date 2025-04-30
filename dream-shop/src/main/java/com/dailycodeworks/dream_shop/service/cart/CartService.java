package com.dailycodeworks.dream_shop.service.cart;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;
import com.dailycodeworks.dream_shop.DreamShopApplication;
import com.dailycodeworks.dream_shop.entity.Cart;
import com.dailycodeworks.dream_shop.entity.CartItem;
import com.dailycodeworks.dream_shop.entity.User;
import com.dailycodeworks.dream_shop.exceptions.ResourceNotFoundException;
import com.dailycodeworks.dream_shop.repository.CartItemRepository;
import com.dailycodeworks.dream_shop.repository.CartRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService{
	private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final AtomicLong cartIdGenerator = new AtomicLong(0);

    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }


    @Transactional
    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cartItemRepository.deleteAllByCartId(id);
        cart.getItems().clear();
        cartRepository.deleteById(id);

    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
    }

    @Override
    public Cart initializeNewCart(User user) {
    	return Optional.ofNullable(getCartByUserId(user.getId())).orElseGet(()->{
    		Cart cart = new Cart();
    		cart.setUser(user);
    		return cartRepository.save(cart);
    	});

    }


	@Override
	public Cart getCartByUserId(Long userId) {
		// TODO Auto-generated method stub
		return cartRepository.findByUserId(userId);
	}



   
}
