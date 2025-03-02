package com.QuizManagementSystem.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.QuizManagementSystem.model.User;
import com.QuizManagementSystem.repository.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}

	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf().disable().authorizeRequests().requestMatchers("/addPassword", "/user/dashboard", "/signup")
				.permitAll().requestMatchers("/user/dashboard/**").hasRole("USER")
				.requestMatchers("/admin/dashboard/**").hasRole("ADMIN").anyRequest().authenticated().and().formLogin()
				.loginPage("/login").permitAll().successHandler((request, response, authentication) -> {
					String role = authentication.getAuthorities().iterator().next().getAuthority();
					if ("ROLE_USER".equals(role)) {
						response.sendRedirect("/user/dashboard");
					} else if ("ROLE_ADMIN".equals(role)) {
						response.sendRedirect("/admin/dashboard");
					} else {
						response.sendRedirect("/signup");
					}
				}).and().logout().permitAll();
		return http.build();
	}

	@Bean
	public UserDetailsService userDetailsService(UserRepository userRepository) {
		return username -> {
			User user = userRepository.findByUsername(username)
					.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

			System.out.println("User fetched: " + user.getUsername());
			System.out.println("Roles fetched: " + user.getRole());
			List<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase()));

			return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
					authorities);
		};

	}
}
