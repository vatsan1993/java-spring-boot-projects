package com.dailycodeworks.dream_shop.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
	private Long id;
	private String token;
	// for refresh token
	private String refreshToken;
}
