package com.nelioalves.cursomc.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Component
public class JWTUtil {

	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private Long expiration;
	
	public String generateToken(String email) {
		return JWT.create()
				.withSubject(email)
				.withExpiresAt(new Date(System.currentTimeMillis() + expiration))
				.sign(Algorithm.HMAC512(secret));
	}
}
