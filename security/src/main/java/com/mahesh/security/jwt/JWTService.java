package com.mahesh.security.jwt;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
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
				.expiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000)).and().signWith(getKey()).compact();
	}

	// gets decoded byte of secretkey
	private SecretKey getKey() {
		byte[] bytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(bytes);
	}

	public String extractUsername(String token) {
		// TODO Auto-generated method stub
		return extractClaims(token, Claims::getSubject);
	}

	public boolean validateToken(String token, UserDetails userByUsername) {
		final String username = extractUsername(token);
		return (username.equals(userByUsername.getUsername()) && !isTokenExpired(token));
	}

	private Date extractExpiration(String token) {
		return extractClaims(token, Claims::getExpiration);
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private <T> T extractClaims(String token, Function<Claims, T> claimresolver) {
		final Claims allClaims = extractAllClaims(token);
		return claimresolver.apply(allClaims);

	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
	}

}
