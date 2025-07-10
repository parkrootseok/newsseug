package com.a301.newsseug.global.config;

import static com.a301.newsseug.domain.member.model.entity.type.RoleType.*;

import com.a301.newsseug.domain.auth.usecase.AuthUseCase;
import com.a301.newsseug.external.jwt.usecase.JwtUseCase;
import com.a301.newsseug.external.oauth.service.CustomOAuth2UserService;
import com.a301.newsseug.external.jwt.filter.JwtAuthenticationFilter;
import com.a301.newsseug.external.jwt.handler.JwtAccessDeniedHandler;
import com.a301.newsseug.external.jwt.handler.JwtAuthenticationEntryPoint;
import com.a301.newsseug.external.oauth.handler.OAuth2AuthenticationSuccessHandler;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    public static final String[] SWAGGER_URI = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.index.html",
            "/webjars/**",
            "/swagger-resources/**"
    };

    private final AuthUseCase authUseCase;
    private final JwtUseCase jwtUseCase;
    private final CustomOAuth2UserService oAuth2UserService;
    private final CorsConfigurationSource corsConfigurationSource;
    private final JwtAccessDeniedHandler accessDeniedHandler;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .headers(
                        header ->
                                header.frameOptions(FrameOptionsConfig::sameOrigin)
                )
                .sessionManagement(
                        sessionManagementConfigurer ->
                                sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(
                        requestConfigurer ->
                                requestConfigurer
                                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                                        .requestMatchers(SWAGGER_URI).permitAll()
                                        .requestMatchers("/newsseug/**").permitAll()
                                        .requestMatchers("/api/v1/members/**").hasRole(ROLE_MEMBER.getRole())
                                        .requestMatchers("/api/v1/folders/**").hasRole(ROLE_MEMBER.getRole())
                                        .requestMatchers("/api/v1/press").hasRole(ROLE_MEMBER.getRole())
                                        .requestMatchers("/api/v1/articles/random").hasRole(ROLE_MEMBER.getRole())
                                        .requestMatchers("/api/v1/auth/**").permitAll()
                                        .requestMatchers("/api/v1/press/*").permitAll()
                                        .requestMatchers("/api/v1/articles/**").permitAll()
                                        .requestMatchers("/api/v1/s3/**").permitAll()
                                        .requestMatchers("/api/v1/search/**").permitAll()
                                        .anyRequest().authenticated()
                )
                .oauth2Login(
                        configurer ->
                                configurer
                                        .userInfoEndpoint(endpointConfig -> endpointConfig.userService(oAuth2UserService))
                                        .successHandler(oAuth2AuthenticationSuccessHandler)
                )
                .addFilterBefore(
                        new JwtAuthenticationFilter(authUseCase, jwtUseCase),
                        UsernamePasswordAuthenticationFilter.class
                )
                .exceptionHandling(
                        handling ->
                                handling.accessDeniedHandler(accessDeniedHandler)
                                        .authenticationEntryPoint(authenticationEntryPoint)
                )
                .build();

    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setExposedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

}
