package com.dailycodeworks.dream_shop.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.dailycodeworks.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class OrderItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Integer quantity;
	private BigDecimal price;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;
	
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	public OrderItem(Order order, Product product, Integer quantity, BigDecimal price  ) {
		super();
		this.quantity = quantity;
		this.price = price;
		this.order = order;
		this.product = product;
	}
	
	
	
}
