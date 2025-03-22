package com.dailycodeworks.dream_shop.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse {
	private String messageString;
	private Object data;
}
