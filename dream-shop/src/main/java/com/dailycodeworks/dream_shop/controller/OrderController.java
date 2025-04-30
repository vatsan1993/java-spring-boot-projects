package com.dailycodeworks.dream_shop.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodeworks.dream_shop.dto.OrderDto;
import com.dailycodeworks.dream_shop.entity.Order;
import com.dailycodeworks.dream_shop.exceptions.ResourceNotFoundException;
import com.dailycodeworks.dream_shop.response.ApiResponse;
import com.dailycodeworks.dream_shop.service.order.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/orders")
public class OrderController {
	private final OrderService orderService;
	
	
	@PostMapping("/order")
	public ResponseEntity<ApiResponse> createOrder(@RequestParam(name="userId") Long userId){
		try {
			Order order = orderService.placeOrder(userId);
			OrderDto orderDto = orderService.convertToDto(order);
			return ResponseEntity.ok(new ApiResponse("Item Order Success!", orderDto));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return  ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse("Error Occurred", e.getMessage()));
			
		}
		
	}
	
	@GetMapping("/{orderId}/order")
	public ResponseEntity<ApiResponse> getOrder(@PathVariable Long orderId){
		try {
			OrderDto order = orderService.getOrder(orderId);
			return ResponseEntity.ok(new ApiResponse("Order found success", order));
		} catch (ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			return  ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					
					.body(new ApiResponse("Error Occurred", e.getMessage()));
			
		}
	}
	
	@GetMapping("/{userId}/orders")
	public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId){
		try {
			List<OrderDto> orders = orderService.getUserOrders(userId);
			return ResponseEntity.ok(new ApiResponse("Order found success", orders));
		} catch (ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			return  ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					
					.body(new ApiResponse("Error Occurred", e.getMessage()));
			
		}
	}
	
}
