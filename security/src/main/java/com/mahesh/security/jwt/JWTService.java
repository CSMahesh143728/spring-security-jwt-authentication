package com.mahesh.security.jwt;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {

	private String secretKey = "";

	public JWTService() throws NoSuchAlgorithmException {

		KeyGenerator generator = KeyGenerator.getInstance("HmacSHA256");
		SecretKey key = generator.generateKey();
		secretKey = Base64.getEncoder().encodeToString(key.getEncoded());

	}

	public String generateToken(String username) {
		Map<String, Object> claims = new HashMap<>();

		return Jwts.builder().claims().add(claims).subject(username).issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 60 * 60 * 30)).and().signWith(getKey()).compact();
	}

	//gets decoded byte of secretkey
	private Key getKey() {
		byte[] bytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(bytes);
	}

	public String extractUsername(String token) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean validateToken(String token, UserDetails userByUsername) {
		// TODO Auto-generated method stub
		return false;
	}

}
