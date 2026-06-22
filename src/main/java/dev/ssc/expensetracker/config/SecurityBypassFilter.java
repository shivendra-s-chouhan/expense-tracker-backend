package dev.ssc.expensetracker.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.List;

@Component
public class SecurityBypassFilter extends GenericFilter{
  @Value("${app.security.enabled:false}")
  private boolean securityEnabled;
  
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    // skip if it's a preflight check
    if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
      chain.doFilter(request, response);
      return;
    }
    if(!securityEnabled){
      UsernamePasswordAuthenticationToken mockAuth = new UsernamePasswordAuthenticationToken("","", List.of(new SimpleGrantedAuthority("USER")));
      SecurityContextHolder.getContext().setAuthentication(mockAuth);
    }
    
    chain.doFilter(request, response);
  }
}
