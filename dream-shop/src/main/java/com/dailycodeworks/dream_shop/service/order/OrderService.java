package com.dailycodeworks.dream_shop.service.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.dailycodeworks.dream_shop.dto.OrderDto;
import com.dailycodeworks.dream_shop.entity.Cart;
import com.dailycodeworks.dream_shop.entity.Order;
import com.dailycodeworks.dream_shop.entity.OrderItem;
import com.dailycodeworks.dream_shop.entity.Product;
import com.dailycodeworks.dream_shop.exceptions.ResourceNotFoundException;
import com.dailycodeworks.dream_shop.repository.OrderRepository;
import com.dailycodeworks.dream_shop.repository.ProductRepository;
import com.dailycodeworks.dream_shop.service.cart.CartService;
import com.dailycodeworks.enums.OrderStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final CartService cartService;
	
	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;

	private final ModelMapper modelMapper;
	
	@Override
	public Order placeOrder(Long userId) {
		// TODO Auto-generated method stub
		Cart cart;
		cart = cartService.getCartByUserId(userId);
		Order order = createOrder(cart);
		List<OrderItem> orderItems = createOrderItems(order, cart);
		order.setOrderItems(new HashSet<>(orderItems));
		order.setTotalAmount(calculateTotalAmount(orderItems));
		Order savedOrder = orderRepository.save(order);
		cartService.clearCart(cart.getId());
		
		return order;
	}
	
	private Order createOrder(Cart cart) {
		 Order order = new Order();
		 order.setUser(cart.getUser());
		 order.setOrderStatus(OrderStatus.PENDING);
		 order.setOrderDate(LocalDate.now());
		 return order;
	}
	
	private List<OrderItem> createOrderItems(Order order, Cart cart){
		return cart.getItems().stream()
				.map(cartItem -> {
					Product product = cartItem.getProduct();
					product.setInventory(product.getInventory() - cartItem.getQuantity());
					productRepository.save(product);
					return new OrderItem(
							order,
							product,
							cartItem.getQuantity(),
							cartItem.getUnitPrice()
					);
				}).toList();
		
		
	}
	
	private BigDecimal calculateTotalAmount(List<OrderItem> orderItems) {
		return orderItems.stream()
				.map(item-> item.getPrice()
						.multiply(new BigDecimal(item.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	@Override
	public OrderDto getOrder(Long orderId) {
		// TODO Auto-generated method stub
		return orderRepository.findById(orderId).map(this::convertToDto).orElseThrow(
				()-> new ResourceNotFoundException("Order not found")
		);
	}
	
	@Override
	public List<OrderDto> getUserOrders(Long userId){
		List<Order> orders = orderRepository.findByUserId(userId);
		
		
		return orders.stream().map(this::convertToDto).toList();
	}
	
	@Override
	public OrderDto  convertToDto(Order order) {
		return modelMapper.map(order, OrderDto.class);
	}

}
