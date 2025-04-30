package com.dailycodeworks.dream_shop.service.user;

import com.dailycodeworks.dream_shop.dto.UserDto;
import com.dailycodeworks.dream_shop.entity.User;
import com.dailycodeworks.dream_shop.request.CreateUserRequest;
import com.dailycodeworks.dream_shop.request.UserUpdateRequest;

public interface IUserService {
	
	User getUserById(Long userId);
	User createUser(CreateUserRequest request);
	void deleteUser(Long userId);
	User updateUser(UserUpdateRequest request, Long userId);
	UserDto convertUserToDto(User user);
	

}
