package dev.ssc.expensetracker.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtils {
  
  private final SecretKey jwtSecretKey;
  private final long jwtExpirationMs;
  
  @Autowired
  public JwtUtils(@Value("${app.jwt.secret:SuperSecretKeyThatMustBeAtLeast32BytesLong!}") String secret, @Value("${app.jwt.expirationMs:86400000}") long expirationMs) {
    this.jwtSecretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.jwtExpirationMs = expirationMs;
  }
  public String generateJwtToken(String email) {
    return Jwts.builder()
               .subject(email)
               .issuedAt(new Date())
               .expiration(new Date((new Date()) .getTime() +  jwtExpirationMs))
               .signWith(jwtSecretKey)
               .compact();
  }
  
}
