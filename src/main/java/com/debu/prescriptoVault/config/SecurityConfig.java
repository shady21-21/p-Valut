package com.debu.prescriptoVault.config;

import com.debu.prescriptoVault.service.JwtUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {
    private final JwtUserDetailsService userDetailsService;
    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtUserDetailsService userDetailsService, JwtFilter jwtFilter){
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(); }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception{
        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return auth.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .cors() // <--- enable CORS support with CorsConfigurationSource bean below
                .and()
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // allow preflight requests
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // public endpoints
                        .requestMatchers("/api/auth/**", "/api/patient/**", "/uploads/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // allow your frontend origin (DEV). Use specific origin instead of "*" in production.
        config.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:3000"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // allow headers your client will send (Authorization for JWT + Content-Type for multipart/form-data)
        config.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type", "X-Requested-With"));
        config.setAllowCredentials(true); // if you plan to use cookies; ok to set true for dev
        config.setExposedHeaders(Arrays.asList("Content-Disposition", "Authorization")); // expose download filename header if needed

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
