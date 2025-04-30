package com.dailycodeworks.dream_shop.service.user;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.dailycodeworks.dream_shop.dto.UserDto;
import com.dailycodeworks.dream_shop.entity.User;
import com.dailycodeworks.dream_shop.exceptions.AlreadyExistsException;
import com.dailycodeworks.dream_shop.exceptions.ResourceNotFoundException;
import com.dailycodeworks.dream_shop.repository.ProductRepository;
import com.dailycodeworks.dream_shop.repository.UserRepository;
import com.dailycodeworks.dream_shop.request.CreateUserRequest;
import com.dailycodeworks.dream_shop.request.UserUpdateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {


	private final UserRepository userRepository;
	private final ModelMapper modelMapper;

 
	
	@Override
	public User getUserById(Long userId) {
		// TODO Auto-generated method stub
		return userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
	}

	@Override
	public User createUser(CreateUserRequest request) {
		// TODO Auto-generated method stub
		return Optional.of(request)
				.filter(user -> !userRepository.existsByEmail(request.getEmail()))
				.map(req ->{
					User user = new User();
					user.setEmail(req.getEmail());
					user.setPassword(req.getPassword());
					user.setFirstName(req.getFirstName());
					user.setLastName(req.getLastName());
					return userRepository.save(user);
				}).orElseThrow(()-> new AlreadyExistsException("Oops "+request.getEmail() +" already exists"));
	}

	@Override
	public User updateUser(UserUpdateRequest request, Long userId) {
		// TODO Auto-generated method stub
		return userRepository.findById(userId).map(
				existingUser -> {
					existingUser.setFirstName(request.getFirstName());
					existingUser.setLastName(request.getLastName());
					return userRepository.save(existingUser);
				}
			).orElseThrow(() -> new ResourceNotFoundException("User not found!"));
		
	}

	@Override
	public void deleteUser(Long userId) {
		// TODO Auto-generated method stub
		userRepository.findById(userId).ifPresentOrElse( userRepository:: delete, ()->{
			throw new ResourceNotFoundException("User not found");
		});
	}
	
	@Override
	public UserDto convertUserToDto(User user) {
		return modelMapper.map(user, UserDto.class);
	}
}
