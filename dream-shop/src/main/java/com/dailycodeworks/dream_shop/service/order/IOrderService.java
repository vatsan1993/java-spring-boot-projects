package com.dailycodeworks.dream_shop.service.order;

import java.util.List;

import com.dailycodeworks.dream_shop.dto.OrderDto;
import com.dailycodeworks.dream_shop.entity.Order;

public interface IOrderService {
	Order placeOrder(Long userId);
	OrderDto getOrder(Long orderId);
	List<OrderDto> getUserOrders(Long userId);
	OrderDto convertToDto(Order order);
}
