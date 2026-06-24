package dev.ssc.expensetracker.user;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
  private UserRepository userRepository;
  
  public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }
  
  @GetMapping
  public Iterable<User> findAll() {
    return userRepository.findAll();
  }
  
  @GetMapping ("/{id}")
  public Optional<User> findById(@PathVariable Integer id) {
    return userRepository.findById(id);
  }

  @PostMapping
  public void save(@RequestBody User user) {
    userRepository.save(user);
  }
  
  @PutMapping
  public void update(@Validated @RequestBody User user) {
    userRepository.save(user);
  }
  
  @DeleteMapping ("/{id}")
  public void deleteById(@PathVariable Integer id) {
    userRepository.deleteById(id);
  }
  
  @GetMapping("/me")
  public ResponseEntity<?> getCurrentUserProfile(){
    String currentEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if("".equals(currentEmail)){
      //get default user (userId = 1)
      Optional<User> defaultUser = userRepository.findByEmail("john.doe@example.com");
      if(defaultUser.isEmpty()){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
      }
      
      User updatedUser = new User (
          defaultUser.get().id(),
          defaultUser.get().name(),
          defaultUser.get().userName(),
          defaultUser.get().email(),
          "", // wipe pwd before sending data to frontend
          defaultUser.get().address(),
          defaultUser.get().phone(),
          defaultUser.get().website(),
          defaultUser.get().company()
      );
      return ResponseEntity.ok(updatedUser);
    }
    return  userRepository.findByEmail(currentEmail)
                .map(user -> {
                  User updatedUser = new User (
                      user.id(),
                      user.name(),
                      user.userName(),
                      user.email(),
                      "", // wipe pwd before sending data to frontend
                      user.address(),
                      user.phone(),
                      user.website(),
                      user.company()
                  );
                  return ResponseEntity.ok(updatedUser);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    
    
  }
  
  
  
}
