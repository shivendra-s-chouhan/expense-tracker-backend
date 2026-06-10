package dev.ssc.expensetracker.config;

public class JwtResponse {
  private String token;
  private String type = "Bearer";
  private String emailId;
  
  public JwtResponse(String token, String emailId) {
    this.token = token;
    this.emailId = emailId;
  }
  
  public String getToken() {
    return token;
  }
  public void setToken(String token) {
    this.token = token;
  }
  public String getType() {
    return type;
  }
  public String getEmailId() {
    return emailId;
  }
  public void setEmailId(String emailId) {
    this.emailId = emailId;
  }
}
