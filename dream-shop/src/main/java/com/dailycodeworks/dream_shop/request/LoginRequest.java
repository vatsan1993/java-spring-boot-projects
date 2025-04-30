package com.dailycodeworks.dream_shop.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data

public class LoginRequest {
	@NotBlank(message = "Invalid Credentials")
	private String email;
	@NotBlank(message = "Invalid Credentials")
	private String password;
}
