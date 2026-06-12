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
  
  //extract email id from token's payload
  public String getEmailFromJwtToken(String token) {
    return Jwts.parser()
               .verifyWith(jwtSecretKey)
               .build()
               .parseSignedClaims(token)
               .getPayload()
               .getSubject();
  }
  
  //validate the token
  public boolean validateJwtToken(String authToken){
    try{
      Jwts.parser().verifyWith(jwtSecretKey).build().parseSignedClaims(authToken);
      return true;
    } catch(Exception e){
      System.err.println("JWT Validation Failed: " + e.getMessage());
    }
    return false;
  }
  
  //remove "Bearer " prefix from header
  public String parseJwtFromHeader(String headerAuth){
    if(headerAuth != null && headerAuth.startsWith("Bearer ")){
      return headerAuth.substring(7);
    }
    return null;
  }
  
}
