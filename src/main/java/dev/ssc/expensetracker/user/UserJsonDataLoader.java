package dev.ssc.expensetracker.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
@Order(1)
public class UserJsonDataLoader implements CommandLineRunner {
  private static final Logger log = LoggerFactory.getLogger(UserJsonDataLoader.class);
  private final UserRepository userRepository;
  private final ObjectMapper objectMapper;
  private final PasswordEncoder passwordEncoder;

  public UserJsonDataLoader(UserRepository userRepository, ObjectMapper objectMapper, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.objectMapper = objectMapper;
    this.passwordEncoder = passwordEncoder;
  }
  
  @Override
  public void run(String... args) throws Exception {
//    if(userRepository.count() > 0) {
//      log.info("Skipping loading of users beacause table already contains data");
//      return;
//    }
    
    try(InputStream inputStream = getClass().getResourceAsStream("/data/users.json")) {
      User[] allUsers =  objectMapper.readValue(inputStream, User[].class);
      for(User user : allUsers) {
        userRepository.findByUserName(user.userName()).ifPresent(existingUser -> {
          // 3. Check if this specific database user is missing a password
          if (existingUser.password() == null || existingUser.password().isBlank()) {
            
            // 4. Encrypt the plain-text password from JSON
            String encryptedPassword = passwordEncoder.encode(user.password());
            
            // 5. Create a new User object with the encrypted hash
            User updatedUser = new User(
                existingUser.id(),
                existingUser.name(),
                existingUser.userName(),
                existingUser.email(),
                encryptedPassword, // Hashes password before DB insert
                existingUser.address(),
                existingUser.phone(),
                existingUser.website(),
                existingUser.company()
            );
            // 6. Save the record back to PostgreSQL (updates the password column)
            userRepository.save(updatedUser);
          }
        });
//        log.info("Loading user {}", user);
//        User userToSave = new User(
//            null,
//            user.name(),
//            user.userName(),
//            user.email(),
//            passwordEncoder.encode(user.password()), // Hashes password before DB insert
//            user.address(),
//            user.phone(),
//            user.website(),
//            user.company()
//        );
//        User savedUser = userRepository.save(userToSave);
//        log.info("Loaded user {}", userRepository.findById(savedUser.id()));
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to load users from Json file", e);
    }
  }
  
}
