package dev.ssc.expensetracker.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class AuthTokenFilter extends GenericFilter {
  
  private final JwtUtils jwtUtils;
  
  public AuthTokenFilter (JwtUtils jwtUtils){
    this.jwtUtils = jwtUtils;
  }
  
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException{
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    String headerAuth = httpRequest.getHeader("Authorization");
    
    //extract token
    String jwt = jwtUtils.parseJwtFromHeader(headerAuth);
    
    if(jwt != null && jwtUtils.validateJwtToken(jwt)){
      String email = jwtUtils.getEmailFromJwtToken(jwt);
      
      // create spring security authentication ticket for the user context
      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
          email,
          null,
          List.of(new SimpleGrantedAuthority("ROLE_USER"))
      );
      
      // authorize the request profile context thread
      SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
    chain.doFilter(request, response);
  }
  
}
