package dev.ssc.expensetracker.user;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
  
  
  
}
