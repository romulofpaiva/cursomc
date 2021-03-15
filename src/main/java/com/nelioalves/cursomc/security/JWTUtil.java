package com.nelioalves.cursomc.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JWTUtil {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private Long expiration;

	public String generateToken(String email) {
		return JWT.create().withSubject(email).withExpiresAt(new Date(System.currentTimeMillis() + expiration))
				.sign(Algorithm.HMAC512(secret));
	}

	public boolean tokenValido(String token) {
		Map<String, Claim> claims = getClaims(token);
		if (claims != null) {
			String username = claims.get("sub").asString();
			Date expirationDate = claims.get("exp").asDate();
			Date now = new Date(System.currentTimeMillis());
			if (username != null && expirationDate != null && now.before(expirationDate)) {
				return true;
			}
		}
		return false;
	}

	private Map<String, Claim> getClaims(String token) {
		try {
			return verifyToken(token).getClaims();
		}
		catch( Exception e ) {
			return new HashMap<String, Claim>();
		}
	}

	private DecodedJWT verifyToken(String token) {
		try {
			return JWT.require(Algorithm.HMAC512(secret)).build().verify(token);
		} catch (Exception e) {
			return null;
		}
	}

	private String getClaim(String token, String claim) {
		try {
			return verifyToken(token).getClaim(claim).asString();
		}
		catch( Exception e ) {
			return "";
		}
	}

	public String getUserName(String token) {
		return getClaim(token, "sub");
	}
}
