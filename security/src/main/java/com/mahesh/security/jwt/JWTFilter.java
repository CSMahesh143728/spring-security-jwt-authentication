package com.mahesh.security.jwt;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mahesh.security.service.MyUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTFilter extends OncePerRequestFilter {

	private final JWTService service;

	private final ApplicationContext appContext;

	public JWTFilter(JWTService service, ApplicationContext appContext) {
		this.service = service;
		this.appContext = appContext;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// Extract Authorization Header
		String authHeader = request.getHeader("Authorization");

		// To get Token
		String token = null;

		// To get Username
		String username = null;

		// Get token and username
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
			username = service.extractUsername(token);
		}

		// Using Security context holder get authentication details if not authenticated
		// then get username from our userdetails service validate it and then set the
		// authentication object
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userByUsername = appContext.getBean(MyUserDetailsService.class).loadUserByUsername(username);
			if (service.validateToken(token, userByUsername)) {
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						userByUsername, null, userByUsername.getAuthorities());
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		}

		filterChain.doFilter(request, response);

	}

}
