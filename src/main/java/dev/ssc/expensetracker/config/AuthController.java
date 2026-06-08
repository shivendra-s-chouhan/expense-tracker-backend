package dev.ssc.expensetracker.config;


import dev.ssc.expensetracker.user.User;
import dev.ssc.expensetracker.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"}) //Allow CORS for frontend during development
public class AuthController {
  
  //Dependencies are marked final for immutability
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  
  //Constructor injection
  public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }
  
  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@RequestBody RegisterRequestDTO signupData){
    if(userRepository.findByEmail(signupData.getEmail())!=null){
      return ResponseEntity.badRequest().body("Username already exists");
    }
    if(userRepository.findByEmail(signupData.getEmail())!=null){
      return ResponseEntity.badRequest().body("Email already registered");
    }
    
    User newUser = new User(
        null,
        null,
        signupData.getUsername(),
        signupData.getEmail(),
        passwordEncoder.encode(signupData.getPassword()),
        null,
        null,
        null,
        null
    );
    userRepository.save(newUser);
    return ResponseEntity.ok("User registered successfully" + newUser);
  }
}
