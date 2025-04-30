package com.dailycodeworks.dream_shop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodeworks.dream_shop.dto.UserDto;
import com.dailycodeworks.dream_shop.entity.User;
import com.dailycodeworks.dream_shop.exceptions.ResourceNotFoundException;
import com.dailycodeworks.dream_shop.request.CreateUserRequest;
import com.dailycodeworks.dream_shop.request.UserUpdateRequest;
import com.dailycodeworks.dream_shop.response.ApiResponse;
import com.dailycodeworks.dream_shop.service.user.IUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {
	private final IUserService userService;
	
	@GetMapping("/{userId}/user")
	public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId){
		try {
			User user = userService.getUserById(userId);
			UserDto userDto = userService.convertUserToDto(user);
			
			return ResponseEntity.ok(new ApiResponse("Get User success!", userDto));
		} catch (ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body(new ApiResponse(e.getMessage(), null));
		}
	}
	
	@PostMapping("/add")
	public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request){
		try {
			User user = userService.createUser(request);
			UserDto userDto = userService.convertUserToDto(user);
			
			return ResponseEntity.ok(new ApiResponse("Create User Success", userDto));
		} catch (Exception e) {

			return ResponseEntity
					.status(HttpStatus.CONFLICT)
					.body(new ApiResponse(e.getMessage(), null));
		}
		
	}
	@PutMapping("/{userId}/update")
	public ResponseEntity<ApiResponse> updateUser(@RequestBody UserUpdateRequest request, @PathVariable Long userId){
		try {
			User user = userService.updateUser(request, userId);
			UserDto userDto = userService.convertUserToDto(user);
			return ResponseEntity.ok(new ApiResponse("Update User Success", userDto));
		} catch (Exception e) {

			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body(new ApiResponse(e.getMessage(), null));
		}
		
	}
	
	
	@DeleteMapping("/{userId}/delete")
	public ResponseEntity<ApiResponse> deleteUser( @PathVariable Long userId){
		try {
			userService.deleteUser( userId);
			return ResponseEntity.ok(new ApiResponse("Deelte User Success", null));
		} catch (Exception e) {

			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body(new ApiResponse(e.getMessage(), null));
		}
		
	}
}
