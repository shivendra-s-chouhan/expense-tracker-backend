package dev.ssc.expensetracker.config;

import dev.ssc.expensetracker.config.SecurityBypassFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@EnableWebSecurity
public class SecurityConfig   {
  private final SecurityBypassFilter securityByPassFilter;
  private final AuthTokenFilter authTokenFilter;
  
  public SecurityConfig(SecurityBypassFilter securityByPassFilter, AuthTokenFilter authTokenFilter){
    this.securityByPassFilter = securityByPassFilter;
    this.authTokenFilter = authTokenFilter;
  }
  
  @Bean
  public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder(); //safely hashes user passwords
  }
  
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(csrf -> csrf.disable()) //Disabled for local development API testing
        .authorizeHttpRequests(auth -> auth
                                           .requestMatchers("/api/auth/**").permitAll() //Allow unauthenticated access to auth endpoints registration/login
                                           .anyRequest().authenticated() //Require authentication for all other endpoints
        )
        .addFilterBefore(securityByPassFilter, UsernamePasswordAuthenticationFilter.class) //Add our custom filter to bypass security at the front of the filter chain
        .addFilterAfter(authTokenFilter, SecurityBypassFilter.class);  //2nd filter
    return http.build();
  }
  
  
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    
    //Allow react local server origin
    configuration.setAllowedOrigins(List.of("http://localhost:5173"));
    
    //Allow std http methods
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    
    //Allow all headers so it accepts 'Authorization Bearer' header
    configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Cache-Control"));
    
    //Allow cookies/auth sessions
    configuration.setAllowCredentials(true);
    
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration); //apply rules to all end points
    return source;
  }
}
