package com.dailycodeworks.dream_shop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodeworks.dream_shop.request.LoginRequest;
import com.dailycodeworks.dream_shop.response.ApiResponse;
import com.dailycodeworks.dream_shop.response.JwtResponse;
import com.dailycodeworks.dream_shop.security.jwt.AuthTokenFilter;
import com.dailycodeworks.dream_shop.security.jwt.JwtUtils;
import com.dailycodeworks.dream_shop.security.user.ShopUserDetails;
import com.dailycodeworks.dream_shop.security.user.ShopUserDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/auth")
public class AuthController {
	private final AuthenticationManager authenticationManager;
	private final JwtUtils jwtUtils;

	@PostMapping("/login")
	public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest){
		System.out.println("Entered auth");
		try {
			System.out.println("Entered email "+loginRequest.getEmail()+" "+loginRequest.getPassword());
			Authentication  authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtUtils.generateTokenForUser(authentication);
			ShopUserDetails userDetails = (ShopUserDetails) authentication.getPrincipal();
			JwtResponse jwtResponse = new JwtResponse(userDetails.getId(), jwt);
			return ResponseEntity.ok(new ApiResponse("Login Successful", jwtResponse));
		} catch (AuthenticationException e) {
			System.out.println("Email or password problem " + e.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("Invalid Login credentials. Please try again", null));
			
		}
		
		
	}
}
