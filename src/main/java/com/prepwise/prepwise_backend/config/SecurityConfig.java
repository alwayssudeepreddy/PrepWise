package com.prepwise.prepwise_backend.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.prepwise.prepwise_backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.prepwise.prepwise_backend.security.JwtAuthenticationFilter;


@Configuration
public class SecurityConfig {
     
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            com.prepwise.prepwise_backend.entity.User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRole().name())
                    .build();
        };
    }

   @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http)
        throws Exception {

    http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth

                    .requestMatchers("/api/auth/**").permitAll()

                    .requestMatchers(HttpMethod.GET,
                            "/api/subjects", "/api/subjects/**").hasAnyRole("STUDENT","ADMIN")

                    .requestMatchers(HttpMethod.POST,
                            "/api/subjects", "/api/subjects/**").hasRole("ADMIN")

                    .requestMatchers(HttpMethod.PUT,
                            "/api/subjects", "/api/subjects/**").hasRole("ADMIN")

                    .requestMatchers(HttpMethod.DELETE,
                            "/api/subjects", "/api/subjects/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST,
                            "/api/units", "/api/units/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET,
                            "/api/units", "/api/units/**").hasAnyRole("ADMIN","STUDENT")
                    .requestMatchers(HttpMethod.PUT,
                            "/api/units", "/api/units/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE,
                            "/api/units", "/api/units/**").hasRole("ADMIN")

                    .requestMatchers(HttpMethod.POST,
                            "/api/chapters", "/api/chapters/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET,
                            "/api/chapters", "/api/chapters/**").hasAnyRole("ADMIN","STUDENT")
                    .requestMatchers(HttpMethod.PUT,
                            "/api/chapters", "/api/chapters/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE,
                            "/api/chapters", "/api/chapters/**").hasRole("ADMIN")

                    .requestMatchers(HttpMethod.POST,
                            "/api/topics", "/api/topics/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET,
                            "/api/topics", "/api/topics/**").hasAnyRole("ADMIN","STUDENT")
                    .requestMatchers(HttpMethod.PUT,
                            "/api/topics", "/api/topics/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE,
                            "/api/topics", "/api/topics/**").hasRole("ADMIN")

                    .requestMatchers(HttpMethod.POST,
                            "/api/questions", "/api/questions/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET,
                            "/api/questions", "/api/questions/**").hasAnyRole("ADMIN","STUDENT")
                    .requestMatchers(HttpMethod.PUT,
                            "/api/questions", "/api/questions/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE,
                            "/api/questions", "/api/questions/**").hasRole("ADMIN")

                    .requestMatchers(HttpMethod.POST,
                            "/api/tests/**").hasAnyRole("STUDENT", "ADMIN")
                    .requestMatchers(HttpMethod.GET,
                            "/api/tests/**").hasAnyRole("STUDENT", "ADMIN")

                    .anyRequest().authenticated()
            )

            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .addFilterBefore(jwtAuthenticationFilter,
                    UsernamePasswordAuthenticationFilter.class);

    return http.build();
}

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

  
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}