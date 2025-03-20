package com.dailycodeworks.dream_shop.exceptions;

public class ProductNotFoundException extends RuntimeException  {
	public ProductNotFoundException(String message) {
		super(message);
	}
}
