package com.demo.enotes_api.service.impl;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.demo.enotes_api.entity.User;
import com.demo.enotes_api.exception.JwtAuthenticationException;
import com.demo.enotes_api.exception.JwtTokenExpiredException;
import com.demo.enotes_api.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService {
	
	private String secretKey = "";
	
	public JwtServiceImpl() {
		try {
			
			KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
			SecretKey sk = keyGen.generateKey();
			secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String generateToken(User user) {
		
		Map<String,Object> claims = new HashMap<>();
		claims.put("role", user.getRoles());
		claims.put("status", user.getStatus().getIsActive());
		
		
		String token = Jwts.builder()
		.claims().add(claims)
		.subject(user.getEmail())
		.issuedAt(new Date(System.currentTimeMillis()))
		.expiration(new Date(System.currentTimeMillis()+ 60*60*60*10))
		.and()
		.signWith(getKey())
		.compact();
		
		return token;
	}

	private Key getKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	@Override
	public String extractUsername(String token) {
		Claims claims = extractClaims(token);
		return claims.getSubject();
	}
	
	public String role(String token) {
		Claims claims = extractClaims(token);
		String role = (String)claims.get("role");
		return role;
	}

	private Claims extractClaims(String token) {
		try {
		return Jwts.parser()
				.verifyWith(decryptKey(secretKey))
				.build()
				.parseSignedClaims(token)
				.getPayload();
		}
		catch(ExpiredJwtException e) {
			throw new JwtTokenExpiredException("Token is Expired.");
		}
		catch(JwtException e) {
			throw new JwtAuthenticationException("Invalid Jwt Token.");
		}
		catch(Exception e) {
			throw e;
		}
		
	}

	private SecretKey decryptKey(String secretKey) {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	@Override
	public Boolean validateToken(String token, UserDetails userDetails) {
		String username = extractUsername(token);
		Boolean isExpired = isTokenExpired(token);
		
		if(username.equalsIgnoreCase(userDetails.getUsername()) && !isExpired) {
			return true;
		}
		
		return false;
	}

	private Boolean isTokenExpired(String token) {
		Claims claims = extractClaims(token);
		Date expirationDate = claims.getExpiration();
	    return expirationDate.before(new Date());
	}

}
