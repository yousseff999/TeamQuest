package org.example.config;

import org.example.Services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // Security Filter Chain to handle CORS and other security configurations
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors() // Enable CORS
                .and()
                .csrf().disable()  // Disable CSRF protection (for stateless API)
                .authorizeRequests()
                .antMatchers("/api/auth/**", "/User/**", "/User/addUser").permitAll()  // Public access to /User/addUser
                .antMatchers(HttpMethod.PUT, "/Events/*/upload-image").permitAll()
                .antMatchers("/Event/**", "/Event/all","/Event/update/","/Event/delete/").permitAll()
                .antMatchers("/Team/**").permitAll()
                .antMatchers("/User/**").permitAll()
                .antMatchers("/Department/**").permitAll()
                .antMatchers("/Challenge/**").permitAll()
                .antMatchers("/Activity/**").permitAll()
                .antMatchers("/Portfolio/**").permitAll()
                .antMatchers("/Rank/**").permitAll()
                .antMatchers("/Feedback/**").permitAll()
                .antMatchers("/Defi/**").permitAll()
                .antMatchers("/api/**").permitAll()
                .antMatchers("/api/stats/**").permitAll()
                .antMatchers("/ws/**", "/topic/**", "/app/**", "/user/**", "/queue/**").permitAll()
                .anyRequest().authenticated()  // Secure other endpoints
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // Stateless session
                .and()
                .build();
    }

    // Password Encoder Bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Use BCrypt password encoder
    }

    // Authentication Provider Bean
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);  // Set custom user details service
        provider.setPasswordEncoder(passwordEncoder());  // Set password encoder
        return provider;
    }

    // Authentication Manager Bean
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authenticationProvider())
                .build();
    }

    // CORS Configuration
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200")); // Allow Angular frontend
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true); // Allow credentials (cookies)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply to all paths
        return source;
    }
}
