package com.umcs.enterprise.auth;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
@EnableWebSecurity
@RequiredArgsConstructor
public class JwtSecurity implements WebMvcConfigurer {

	@Bean
	public AuthenticationManager authenticationManager() {
		return new ProviderManager(new DaoAuthenticationProvider());
	}

//	@Bean("mvcHandlerMappingIntrospector") public HandlerMappingIntrospector handlerMappingIntrospector(ApplicationContext ctx){
//		HandlerMappingIntrospector handlerMappingIntrospector = new HandlerMappingIntrospector();
//		handlerMappingIntrospector.setApplicationContext(ctx);
//		return handlerMappingIntrospector;
//	}



	@NonNull
	private final JwtFilter jwtFilter;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**");
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.cors()
			.and()
			.csrf()
			.disable()

			.authorizeHttpRequests(r ->
				r.requestMatchers("/graphql*", "/graphiql*", "/hello").permitAll().anyRequest().hasRole("ADMIN")
			)
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		//		http.addFilter(BearerTokenAuthenticationFilter.class);

		return http.build();
		// ...
	}
}
