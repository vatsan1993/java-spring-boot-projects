package com.dailycodeworks.dream_shop.data;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.dailycodeworks.dream_shop.entity.User;
import com.dailycodeworks.dream_shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationEvent> {

    private final UserRepository userRepository;

    @Override
	public void onApplicationEvent(ApplicationEvent event) {
		// TODO Auto-generated method stub
		createDeaultUsersIfNotExists();
	}

	private void createDeaultUsersIfNotExists() {
		// TODO Auto-generated method stub
		for(int i = 0; i< 5; i++) {
			String defaultEmail = "user"+i+"@email.com";
			if(userRepository.existsByEmail(defaultEmail)) {
				continue;
			}
			User user = new User();
			user.setFirstName("The user");
			user.setLastName("user "+ i);
			user.setEmail(defaultEmail);
			user.setPassword("123456");
			userRepository.save(user);
			System.out.println("Default vet user "+ i +" created successfully");
		}
	}
	
	

}
