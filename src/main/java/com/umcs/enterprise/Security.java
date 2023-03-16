package com.umcs.enterprise;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
//@EnableMethodSecurity()
public class Security {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public InMemoryUserDetailsManager userDetailsService() {
		UserDetails user = User
			.withUsername("user")
			.password(passwordEncoder().encode("user"))
			.roles("USER")
			.build();

		UserDetails admin = User
			.withUsername("admin")
			.password(passwordEncoder().encode("admin"))
			.roles("ADMIN")
			.build();
		return new InMemoryUserDetailsManager(user, admin);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf()
			.disable()
			.authorizeHttpRequests()
			.requestMatchers("/admin/**")
			.hasRole("ADMIN")
			.requestMatchers("/graphql*")
			.permitAll()
			.requestMatchers("/login*")
			.anonymous()
			.anyRequest()
			.authenticated()
			.and()
			.formLogin();

		return http.build();
		// ...
	}
}
