package com.dailycodeworks.dream_shop.data;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.dailycodeworks.dream_shop.entity.Role;
import com.dailycodeworks.dream_shop.entity.User;
import com.dailycodeworks.dream_shop.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.Set;

@Transactional
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationEvent> {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

 

    @Override
	public void onApplicationEvent(ApplicationEvent event) {
		// TODO Auto-generated method stub
    	Set<String> defaultRoles = Set.of("ROLE_ADMIN", "ROLE_USER");
		
		createDefaultRoleIfNotExists(defaultRoles);
		createDeaultUsersIfNotExists();
		createDeaultAdminIfNotExists();
	}

	private void createDeaultUsersIfNotExists() {
		// TODO Auto-generated method stub
		Role userRole = roleRepository.findByName("ROLE_USER").get();
		for(int i = 0; i< 5; i++) {
			String defaultEmail = "user"+i+"@email.com";
			if(userRepository.existsByEmail(defaultEmail)) {
				continue;
			}
			User user = new User();
			user.setFirstName("The user");
			user.setLastName("user "+ i);
			user.setEmail(defaultEmail);
			user.setPassword(passwordEncoder.encode("123456"));
			user.setRoles(Set.of(userRole));
			userRepository.save(user);
			System.out.println("Default vet user "+ i +" created successfully");
		}
	}
	
	
	private void createDeaultAdminIfNotExists() {
		// TODO Auto-generated method stub
		for(int i = 0; i< 2; i++) {
			String defaultEmail = "admin"+i+"@email.com";
			Role adminRole = roleRepository.findByName("ROLE_Admin").get();
			if(userRepository.existsByEmail(defaultEmail)) {
				continue;
			}
			User user = new User();
			user.setFirstName("Admin");
			user.setLastName("user "+ i);
			user.setEmail(defaultEmail);
			user.setPassword(passwordEncoder.encode("123456"));
			user.setRoles(Set.of(adminRole));
			userRepository.save(user);
			System.out.println("Default vet Admin "+ i +" created successfully");
		}
	}
	
	
	private void createDefaultRoleIfNotExists(Set<String> roles) {
		roles.stream()
		.filter(role -> roleRepository.findByName(role).isEmpty()).
		map(Role::new).forEach(roleRepository::save);
	}
	

}
