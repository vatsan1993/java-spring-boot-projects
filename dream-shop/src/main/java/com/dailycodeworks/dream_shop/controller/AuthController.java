package com.dailycodeworks.dream_shop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodeworks.dream_shop.entity.RefreshToken;
import com.dailycodeworks.dream_shop.entity.User;
import com.dailycodeworks.dream_shop.exceptions.TokenRefreshException;
import com.dailycodeworks.dream_shop.repository.UserRepository;
import com.dailycodeworks.dream_shop.request.LoginRequest;
import com.dailycodeworks.dream_shop.request.TokenRefreshRequest;
import com.dailycodeworks.dream_shop.response.ApiResponse;
import com.dailycodeworks.dream_shop.response.JwtResponse;
import com.dailycodeworks.dream_shop.security.jwt.AuthTokenFilter;
import com.dailycodeworks.dream_shop.security.jwt.JwtUtils;
import com.dailycodeworks.dream_shop.security.user.ShopUserDetails;
import com.dailycodeworks.dream_shop.security.user.ShopUserDetailsService;
import com.dailycodeworks.dream_shop.service.refreshToken.IRefreshTokenService;
import com.dailycodeworks.dream_shop.service.refreshToken.RefreshTokenService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/auth")
public class AuthController {
	private final AuthenticationManager authenticationManager;
	private final JwtUtils jwtUtils;
	private final IRefreshTokenService refreshTokenService;
	
//	@PostMapping("/login")
//	public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest){
//		System.out.println("Entered auth");
//		try {
//			System.out.println("Entered email "+loginRequest.getEmail()+" "+loginRequest.getPassword());
//			Authentication  authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
//			
//			SecurityContextHolder.getContext().setAuthentication(authentication);
//			String jwt = jwtUtils.generateTokenForUser(authentication);
//			ShopUserDetails userDetails = (ShopUserDetails) authentication.getPrincipal();
//			JwtResponse jwtResponse = new JwtResponse(userDetails.getId(), jwt);
//			return ResponseEntity.ok(new ApiResponse("Login Successful", jwtResponse));
//		} catch (AuthenticationException e) {
//			System.out.println("Email or password problem " + e.getMessage());
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("Invalid Login credentials. Please try again", null));
//			
//		}
//		
//		
//	}
	
	@PostMapping("/login")
	public ResponseEntity<ApiResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
	    try {
	    	Authentication authentication = authenticationManager.authenticate(
		            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
	
		    SecurityContextHolder.getContext().setAuthentication(authentication);
	
		    String jwt = jwtUtils.generateTokenForUser(authentication);
		    ShopUserDetails userDetails = (ShopUserDetails) authentication.getPrincipal();
	
		    RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
	
		    return ResponseEntity.ok(new ApiResponse("Login Successful",new JwtResponse(userDetails.getId(), jwt, refreshToken.getToken())));
	    }catch (AuthenticationException e) {
			System.out.println("Email or password problem " + e.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("Invalid Login credentials. Please try again", null));
			
		}
	}
	
	
	@PostMapping("/refresh")
	public ResponseEntity<ApiResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
	    String requestRefreshToken = request.getRefreshToken();
	    String email = request.getEmail();
	    String password = request.getPassword();

	    System.out.println("Received Refresh Token: " + requestRefreshToken);
	    System.out.println("Received Email: " + email);

	    try {
	        return refreshTokenService.findByToken(requestRefreshToken)
	                .map(refreshToken -> {
	                    System.out.println("Refresh Token Found: " + refreshToken.getToken());

	                    // Verify expiration
	                    RefreshToken verifiedToken;
	                    try {
	                        verifiedToken = refreshTokenService.verifyExpiration(refreshToken);
	                    } catch (Exception e) {
	                        System.out.println("Exception during token expiration verification: " + e.getMessage());
	                        e.printStackTrace();
	                        throw new TokenRefreshException(requestRefreshToken, "Refresh token verification failed.");
	                    }

	                    if (verifiedToken.getUser() == null) {
	                        System.out.println("User is null for this refresh token.");
	                        throw new TokenRefreshException(requestRefreshToken, "User not found for this token.");
	                    }

	                    try {
	                        // Authenticate using email and password
	                        Authentication authentication = authenticationManager.authenticate(
	                                new UsernamePasswordAuthenticationToken(email, password));

	                        SecurityContextHolder.getContext().setAuthentication(authentication);

	                        String token = jwtUtils.generateTokenForUser(authentication);
	                        System.out.println("New JWT Generated: " + token);

	                        return ResponseEntity.ok(new ApiResponse("Token refreshed successfully",
	                                new JwtResponse(verifiedToken.getUser().getId(), token, requestRefreshToken)));

	                    } catch (AuthenticationException authEx) {
	                        System.out.println("Authentication Exception: " + authEx.getMessage());
	                        authEx.printStackTrace();
	                        throw new TokenRefreshException(requestRefreshToken, "Authentication failed during token refresh.");
	                    }
	                })
	                .orElseThrow(() -> {
	                    System.out.println("Token not found in database.");
	                    return new TokenRefreshException(requestRefreshToken, "Refresh token is not in the database!");
	                });
	    } catch (Exception e) {
	        System.out.println("Exception in /refresh endpoint: " + e.getMessage());
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error processing refresh token", null));
	    }
	}
	
	
	@PostMapping("/logout")
	public ResponseEntity<ApiResponse> logout(@Valid @RequestBody TokenRefreshRequest request) {
	    String requestRefreshToken = request.getRefreshToken();
	    String email = request.getEmail();

	    System.out.println("Logout Request Received. Email: " + email + ", Refresh Token: " + requestRefreshToken);

	    try {
	        return refreshTokenService.findByToken(requestRefreshToken)
	                .map(refreshToken -> {
	                    // Check if the user associated with the token matches the provided email
	                    if (!refreshToken.getUser().getEmail().equals(email)) {
	                        System.out.println("User email mismatch during logout.");
	                        throw new TokenRefreshException(requestRefreshToken, "User email does not match the token.");
	                    }

	                    // Delete the refresh token
	                    int deletedCount = refreshTokenService.deleteByUserId(refreshToken.getUser().getId());
	                    System.out.println("Refresh tokens deleted: " + deletedCount);

	                    // Clear the security context
	                    SecurityContextHolder.clearContext();

	                    return ResponseEntity.ok(new ApiResponse("User logged out successfully", null));
	                })
	                .orElseThrow(() -> {
	                    System.out.println("Refresh token not found during logout.");
	                    return new TokenRefreshException(requestRefreshToken, "Refresh token is not in the database!");
	                });
	    } catch (Exception e) {
	        System.out.println("Exception in /logout endpoint: " + e.getMessage());
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(new ApiResponse("Error processing logout", null));
	    }
	}


}
