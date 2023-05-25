package com.demo.config;

import static org.springframework.security.config.Customizer.withDefaults;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2AccessTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;

import com.demo.service.impl.UserLoginServiceImpl;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

	private final JpaOAuth2AuthorizationService jpaOAuth2AuthorizationService;
	private final UserLoginServiceImpl userLoginServiceImpl;

	@Bean
	@Order(1)
	SecurityFilterChain asSecurityFilterChain(final HttpSecurity http) throws Exception {

		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
		return http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
				.tokenEndpoint(tokenEndpoint -> tokenEndpoint.accessTokenRequestConverter(new CustomPasswordAuthenticationConverter())
						.authenticationProvider(new CustomPasswordAuthenticationProvider(jpaOAuth2AuthorizationService, tokenGenerator(), userLoginServiceImpl))
						.accessTokenRequestConverters(getConverters()).authenticationProviders(getProviders()))
				.oidc(withDefaults()).and().oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt).build();
	}

	private Consumer<List<AuthenticationProvider>> getProviders() {
		return a -> a.forEach(System.out::println);
	}

	private Consumer<List<AuthenticationConverter>> getConverters() {
		return a -> a.forEach(System.out::println);
	}

	@Bean
	@Order(2)
	SecurityFilterChain appSecurityFilterChain(final HttpSecurity http) throws Exception {
//		return http.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated()).formLogin().disable().build();
		http.cors();
		http.httpBasic().disable();
		http.authorizeHttpRequests().anyRequest().permitAll();
//		http.authorizeHttpRequests().requestMatchers("/demo/**").permitAll();
		http.csrf().disable();
		return http.build();
	}

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder().issuer("http://localhost:9000").authorizationEndpoint("/oauth2/authorize").tokenEndpoint("/oauth2/token")
				.tokenIntrospectionEndpoint("/oauth2/introspect").tokenRevocationEndpoint("/oauth2/revoke").jwkSetEndpoint("/oauth2/jwks")
				.oidcUserInfoEndpoint("/userinfo").build();
	}

	@Bean
	public OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator() {
		NimbusJwtEncoder jwtEncoder = new NimbusJwtEncoder(jwkSource());
		JwtGenerator jwtGenerator = new JwtGenerator(jwtEncoder);
		jwtGenerator.setJwtCustomizer(tokenCustomizer());
		OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
		OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
		return new DelegatingOAuth2TokenGenerator(jwtGenerator, accessTokenGenerator, refreshTokenGenerator);
	}

	@Bean
	OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
		return context -> {
			OAuth2ClientAuthenticationToken principal = context.getPrincipal();
			UserAwareUserDetails user = (UserAwareUserDetails) principal.getDetails();
			Set<String> authorities = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
			if ("access_token".equals(context.getTokenType().getValue())) {
				context.getClaims().claim("authorities", authorities).claim("user", user.getUsername());
			}
		};
	}

	@Bean
	JwtDecoder jwtDecoder(final JWKSource<SecurityContext> jwkSource) {
		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
	}

	@Bean
	JWKSource<SecurityContext> jwkSource() {
		RSAKey rsaKey = generateRsa();
		JWKSet jwkSet = new JWKSet(rsaKey);
		return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
	}

	public static RSAKey generateRsa() {
		KeyPair keyPair = generateRsaKey();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		return new RSAKey.Builder(publicKey).privateKey(privateKey).keyID(UUID.randomUUID().toString()).build();
	}

	static KeyPair generateRsaKey() {
		KeyPair keyPair;
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			keyPair = keyPairGenerator.generateKeyPair();
		} catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
		return keyPair;
	}
}