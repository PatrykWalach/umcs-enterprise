package com.umcs.enterprise.auth;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.umcs.enterprise.user.UserService;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.*;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class OAuthSecurity implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**");
	}

	@Bean
	@Order(1)
	public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
		throws Exception {
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
		http.getConfigurer(OAuth2AuthorizationServerConfigurer.class).oidc(Customizer.withDefaults()); // Enable OpenID Connect 1.0
		http
			// Accept access tokens for User Info and/or Client Registration
			.oauth2ResourceServer(resourceServer -> resourceServer.jwt(Customizer.withDefaults()))
			// Redirect to the login page when not authenticated from the
			// authorization endpoint
			.exceptionHandling(exceptions ->
				exceptions.defaultAuthenticationEntryPointFor(
					new LoginUrlAuthenticationEntryPoint("http://localhost:5173/login"),
					new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
				)
			);

		return http.build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.formLogin(form ->
				form.loginPage("http://localhost:5173/login").permitAll().loginProcessingUrl("/login")
			)
			.authorizeHttpRequests(authorizeHttpRequests ->
				authorizeHttpRequests
					.requestMatchers(HttpMethod.GET)
					.permitAll()
					.requestMatchers("/graphql")
					.permitAll()
					.anyRequest()
					.denyAll()
			)
			.oauth2ResourceServer(resourceServer ->
				resourceServer.jwt(jwt ->
					jwt.jwtAuthenticationConverter(token -> {
						UserDetails userDetails = userDetailsService.loadUserByUsername(token.getSubject());
						List<GrantedAuthority> authorities = new ArrayList<>(userDetails.getAuthorities());

						token
							.getClaimAsStringList("scope")
							.stream()
							.map(scope -> "SCOPE_" + scope)
							.map(SimpleGrantedAuthority::new)
							.forEach(authorities::add);

						return new JwtAuthenticationToken(token, authorities);
					})
				)
			);

		return http.build();
	}

	@Bean
	public RegisteredClientRepository registeredClientRepository() {
		RegisteredClient oidcClient = RegisteredClient
			.withId(UUID.randomUUID().toString())
			.clientId("bookstore")
			.clientSecret("{noop}bookstoreSecret")
			.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
			.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
			.clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
			.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
			.redirectUri("http://localhost:5173/login/callback")
			.scope(OidcScopes.OPENID)
			.scope(OidcScopes.PROFILE)
			.scope("read")
			.scope("write")
			.clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).build())
			.build();

		return new InMemoryRegisteredClientRepository(oidcClient);
	}

	@Bean
	public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
	}

	@Bean
	public JWKSource<SecurityContext> jwkSource() throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(2048);

		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		RSAKey rsaKey = new RSAKey.Builder(publicKey)
			.privateKey(privateKey)
			.keyID(UUID.randomUUID().toString())
			.build();
		JWKSet jwkSet = new JWKSet(rsaKey);
		return new ImmutableJWKSet<>(jwkSet);
	}

	@NonNull
	private final UserDetailsService userDetailsService;

	@Bean
	public AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder().build();
	}
}