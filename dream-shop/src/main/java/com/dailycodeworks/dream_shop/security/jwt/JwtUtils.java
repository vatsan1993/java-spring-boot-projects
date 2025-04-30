package com.dailycodeworks.dream_shop.security.jwt;

import java.security.Key;import java.util.Base64.Decoder;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.dailycodeworks.dream_shop.security.user.ShopUserDetails;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtUtils {
	
	// we need to setup some properties for these variables in the properties file.
	@Value("${auth.token.jwtSecret}")
	private String jwtSecret;
	@Value("${auth.token.expirationInMilis}")
	private int expirationTime;
	// Note Authentication is an interface which is from Spring security core. not from other 
	public String generateTokenForUser(Authentication authentication) {
		// this gets the logged in user.
		ShopUserDetails userPrincipal = (ShopUserDetails) authentication.getPrincipal();
		
		// getting all the user's authority
		List<String> roles = userPrincipal.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority).toList();
//		encoding the details into jwt token
//		jwt token us created by hashing the details of a user and a secret key.
//		jwt needs subject, claims, issuedAt- the time at which the jwt is created
//		, the expirationTime.
//		any information that we want to encode into the jwt token, we simply pass 
//		it with the claim method. In this example we are only encoding id, email and roles
//		these are available in the ShopUserDetails class. If we also want the firstName,
//		we simple have a private variable in ShopUserDetails and copy the value from the
//		User entity object.
		return Jwts.builder()
				.setSubject(userPrincipal.getEmail())
				.claim("id",userPrincipal.getId())
				.claim("roles", roles)
				.setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime() + expirationTime))
				.signWith(key(), SignatureAlgorithm.HS256).compact();
	}
	// This method return the secret needed by the jwt builder.
	// The Key is an interface from java.security package
	private Key key() {
		// TODO Auto-generated method stub
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}
	
	
	// A method that extracts userName(in this case email) from the jwt token
	public String getUsernameFromJwtToken(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(key())
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}
	
	// method to validate the jwt token.
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
			.setSigningKey(key())
			.build()
			.parseClaimsJws(token);
		} catch (SignatureException | ExpiredJwtException | UnsupportedJwtException | MalformedJwtException
				| IllegalArgumentException e) {
			// TODO Auto-generated catch block
			throw new JwtException(e.getMessage());
		}
		return true;
	}
}
