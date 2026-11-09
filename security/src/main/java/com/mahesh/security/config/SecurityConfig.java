package com.mahesh.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.mahesh.security.service.MyUserDetailsService;
import com.mahesh.security.jwt.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final JWTFilter jwtfilter;

	public SecurityConfig(JWTFilter jwtfilter) {
		this.jwtfilter=jwtfilter;
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider provider) {
		return http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(request -> request.requestMatchers("/register", "/login").permitAll()
						.anyRequest().authenticated())
				//.httpBasic(Customizer.withDefaults())
				.authenticationProvider(provider)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtfilter, UsernamePasswordAuthenticationFilter.class).build();
	}

//	UserDetails user1= User
//			.withDefaultPasswordEncoder()
//			.username("Varun")
//			.password("V@1234")
//			.roles("USER")
//			.build();
//	
//	@Bean
//	public UserDetailsService userDetailsService() {
//		return new InMemoryUserDetailsManager(user1);
//	}

	@Bean
	AuthenticationProvider authenticationProvider(MyUserDetailsService detailsService) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(detailsService);
		provider.setPasswordEncoder(new BCryptPasswordEncoder(10));
		return provider;
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
		return configuration.getAuthenticationManager();
	}

}
