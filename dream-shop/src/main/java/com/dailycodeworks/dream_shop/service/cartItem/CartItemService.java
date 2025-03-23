package com.dailycodeworks.dream_shop.service.cartItem;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dailycodeworks.dream_shop.entity.Cart;
import com.dailycodeworks.dream_shop.entity.CartItem;
import com.dailycodeworks.dream_shop.entity.Product;
import com.dailycodeworks.dream_shop.exceptions.ResourceNotFoundException;
import com.dailycodeworks.dream_shop.repository.CartItemRepository;
import com.dailycodeworks.dream_shop.repository.CartRepository;
import com.dailycodeworks.dream_shop.service.cart.ICartService;
import com.dailycodeworks.dream_shop.service.product.IProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class CartItemService implements ICartItemService{

    private final CartRepository cartRepository;
	
	private final CartItemRepository cartItemRepository;
	@Autowired
	private final IProductService productService;
	
	@Autowired
	private final ICartService cartService;


	@Override
	public void addItemToCart(Long cartId, Long productId, int quantity) {
		// TODO Auto-generated method stub
		// 1. get the cart
		// 2. get the product
		// 3. check of product is already in the cart.
		// 4. if yes, then increase the quantity with requested quantity
		// 5. If no, then create new CartItem
		
		Cart cart = cartService.getCart(cartId);
		Product product = productService.getProductById(productId);
		
		CartItem cartItem = cart.getCartItems()
				.stream()
				.filter(item -> item.getProduct().getId().equals(productId))
				.findFirst().orElse(new CartItem());
		
		if(cartItem.getId() == null) {
			cartItem.setCart(cart);
			cartItem.setProduct(product);
			cartItem.setQuantity(quantity);
			cartItem.setUnitPrice(product.getPrice());
		} else {
			cartItem.setQuantity(cartItem.getQuantity() + quantity);
		}
		cartItem.setTotalPrice();
		cart.addItem(cartItem);
		cartItemRepository.save(cartItem);
		cartRepository.save(cart);
	}

	@Override
	public void removeItemFromCart(Long cartId, Long productId) {
		// TODO Auto-generated method stub
		Cart cart = cartService.getCart(cartId);
		CartItem itemToRemove = getCartItem(cartId, productId);
		cart.removeItem(itemToRemove);
		cartRepository.save(cart);
	}

	@Override
	public void updateItemQuantity(Long cartId, Long productId, int quantity) {
		// TODO Auto-generated method stub
		Cart cart = cartService.getCart(cartId);
		cart.getCartItems().stream()
				.filter(item->item.getProduct().getId().equals(productId))
				.findFirst()
				.ifPresent( item ->{
					item.setQuantity(quantity);
					item.setUnitPrice(item.getProduct().getPrice());
					item.setTotalPrice();
					
				});
		BigDecimal totalPrice = cart.getTotalAmount();
		cart.setTotalAmount(totalPrice);
		cartRepository.save(cart);
	}
	
	// helper method
	@Override
	public CartItem getCartItem(Long cartId, Long productId){
		Cart cart = cartService.getCart(cartId);
		CartItem cartItem = cart.getCartItems().stream()
				.filter(item->item.getProduct().getId().equals(productId))
				.findFirst().orElseThrow(()->new ResourceNotFoundException("Product Not found!"));
	
		return cartItem;
	}

}
