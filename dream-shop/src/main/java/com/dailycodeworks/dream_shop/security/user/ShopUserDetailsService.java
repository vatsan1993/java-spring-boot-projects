package com.dailycodeworks.dream_shop.security.user;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dailycodeworks.dream_shop.entity.User;
import com.dailycodeworks.dream_shop.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ShopUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = (User) Optional.of(userRepository.findByEmail(email))
				.orElseThrow(() ->new UsernameNotFoundException("User not found"));
		return ShopUserDetails.buildUserDetails(user);
	}

}
