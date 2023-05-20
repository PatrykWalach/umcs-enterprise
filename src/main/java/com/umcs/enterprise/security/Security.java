package com.umcs.enterprise.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class Security {

	@Bean
	public SecurityFilterChain httpSecurity(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests()
			.antMatchers(HttpMethod.GET, "/**")
			.permitAll()
			.antMatchers(HttpMethod.POST, "/**")
			.permitAll()
			.anyRequest()
			.permitAll();

		return http.build();
	}
}
