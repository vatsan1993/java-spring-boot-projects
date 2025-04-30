package com.dailycodeworks.dream_shop.security.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dailycodeworks.dream_shop.security.user.ShopUserDetailsService;

import io.jsonwebtoken.JwtException;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthTokenFilter extends OncePerRequestFilter {
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private ShopUserDetailsService userDetailsService;
	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request,
			@NonNull HttpServletResponse response, 
			@NonNull FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String jwt = parseJwt(request);
			if(StringUtils.hasText(jwt) && jwtUtils.validateToken(jwt)) {
				String email = jwtUtils.getUsernameFromJwtToken(jwt);
				
				// checks if the user is in the Database
				UserDetails userDetails = userDetailsService.loadUserByUsername(email);
				
				// Authenticating the user based on the roles.
				Authentication auth = new UsernamePasswordAuthenticationToken( userDetails, null, userDetails.getAuthorities());
			
				//Authenticating the current user.
				SecurityContextHolder.getContext().setAuthentication(auth);
			
			}
		} catch (JwtException e) {
			// TODO Auto-generated catch block
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write(e.getMessage() + ": Invalid or expired token, you may login and try again");
			return;
		}catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write(e.getMessage() );
			return;
		}
		
		filterChain.doFilter(request, response);
		
	}
	
	// This method will receive the token from the client and extracts the jwt token
	private String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");
		if(StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7);
		}
		return null;
	}

}
