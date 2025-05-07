package com.dailycodeworks.dream_shop.security.config;



import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.awt.datatransfer.Transferable;
import java.util.List;

import com.dailycodeworks.dream_shop.security.jwt.AuthTokenFilter;
import com.dailycodeworks.dream_shop.security.jwt.JwtAuthEntryPoint;
import com.dailycodeworks.dream_shop.security.user.ShopUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity // enables web security
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true) // enables method level security
public class ShopConfig {
	private final ShopUserDetailsService userDetailsService;
	private final JwtAuthEntryPoint authEntryPoint;
	
	// adding urls that can be accessed securely.
    private static final List<String> SECURED_URLS =
            List.of("/api/v1/carts/**", "/api/v1/cartItems/**");
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
		
	}
	
//	jwt related config
	@Bean
	public PasswordEncoder passwordEncoder() {
//		used to uncode passwords.
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthTokenFilter authTokenFilter() {
//		Linking the AuthTokenFilter class we created
		return new AuthTokenFilter();
	}
	
	@Bean
	public AuthenticationManager authenticationManager (AuthenticationConfiguration authConfig) throws Exception {
//		This managers the authentication process.
		return authConfig.getAuthenticationManager();
	}
	
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//		linking the userDetailsService 
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
		
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		// applying cors on all 
		 http
	        .cors(cors -> cors.configurationSource(request -> {
	            var corsConfig = new org.springframework.web.cors.CorsConfiguration();
	            corsConfig.setAllowedOrigins(List.of("http://localhost:5173"));
	            corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
	            corsConfig.setAllowedHeaders(List.of("*"));
	            corsConfig.setAllowCredentials(true);
	            return corsConfig;
	        }));
		
		// handling cross site request forgery.

        http.csrf(AbstractHttpConfigurer::disable) // disable csrf    
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPoint))
//                setting session as state less. (does not track the jwt on serverside)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                we are using the requestMatcher with an array of regex to check if the user is reaching any of the mathing url
//                These urls should be only accessible only when authenticated.
//                Any other url should be permitted without authentication.
//                SECURED_URLS is declared above.
                .authorizeHttpRequests(auth ->auth.requestMatchers(SECURED_URLS.toArray(String[]::new)).authenticated()
                        .anyRequest().permitAll());
        
        http.authenticationProvider(daoAuthenticationProvider());
        http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
	}
	
	
	@Bean
    public WebMvcConfigurer corsConfigurer() {
		
//		setting up Cross site request origin 
		
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**") // Apply to all endpoints
                        .allowedOrigins("http://localhost:5173") // Allow this origin
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow these HTTP methods
                        .allowedHeaders("*") // Allow all headers
                        .allowCredentials(true); // Allow credentials
            }
        };
    }
	
}
