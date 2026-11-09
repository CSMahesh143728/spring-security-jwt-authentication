package com.mahesh.security.jwt;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mahesh.security.service.MyUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTFilter extends OncePerRequestFilter {

	private final JWTService service;

	// private final ApplicationContext appContext;

	private final MyUserDetailsService detailsService;

	public JWTFilter(JWTService service, MyUserDetailsService usrDtlService) {
		this.service = service;
		// this.appContext = appContext;
		this.detailsService = usrDtlService;

	}

	@Override
	protected void doFilterInternal(
	        HttpServletRequest request,
	        HttpServletResponse response,
	        FilterChain filterChain)
	        throws ServletException, IOException {

	    String authHeader = request.getHeader("Authorization");

	    System.out.println("STEP 1: JWT Filter executed");
	    System.out.println("Request URI: " + request.getRequestURI());
	    System.out.println("Authorization header present: " + (authHeader != null));

	    if (authHeader != null && authHeader.startsWith("Bearer ")) {

	        String token = authHeader.substring(7);

	        System.out.println("STEP 2: Token extracted");

	        String username = service.extractUsername(token);

	        System.out.println("STEP 3: Username: " + username);

	        if (username != null &&
	                SecurityContextHolder.getContext().getAuthentication() == null) {

	            UserDetails userDetails =
	                    detailsService.loadUserByUsername(username);

	            System.out.println("STEP 4: User loaded");

	            boolean valid = service.validateToken(token, userDetails);

	            System.out.println("STEP 5: Token valid: " + valid);

	            if (valid) {

	                UsernamePasswordAuthenticationToken authentication =
	                        new UsernamePasswordAuthenticationToken(
	                                userDetails,
	                                null,
	                                userDetails.getAuthorities());

	                authentication.setDetails(
	                        new WebAuthenticationDetailsSource()
	                                .buildDetails(request));

	                SecurityContextHolder.getContext()
	                        .setAuthentication(authentication);

	                System.out.println("STEP 6: Authentication set successfully");
	            }
	        }
	    }

	    Authentication authentication =
	            SecurityContextHolder.getContext().getAuthentication();

	    System.out.println("Authentication object: " + authentication);

	    filterChain.doFilter(request, response);
	}

}
