package dev.ssc.expensetracker.category;

import dev.ssc.expensetracker.config.SecurityConfig;
import dev.ssc.expensetracker.user.User;
import dev.ssc.expensetracker.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/categories")
public class CategoryController {
  CategoryRepository categoryRepository;
  UserRepository userRepository;
  public CategoryController(CategoryRepository categoryRepository, UserRepository userRepository) {
    this.categoryRepository = categoryRepository;
    this.userRepository = userRepository;
  }
  
  /* can be used for testing
  
  @GetMapping
  public ResponseEntity<List<Category>> getAllCategories() {
    return ResponseEntity.ok(categoryRepository.findAll());
  }
  @GetMapping("/{id}")
  public ResponseEntity<Category> getCategoryById(@PathVariable Integer id) {
    Optional<Category> category = categoryRepository.findById(id);
    if (category.isPresent()) {
      return ResponseEntity.ok(category.get());
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }
  
   */
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  void addCategory(@Validated @RequestBody Category category) {
    categoryRepository.save(category);
  }
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PutMapping("/{id}")
  void updateCategory(@Validated @RequestBody Category category, @PathVariable Integer id) {
    categoryRepository.save(category);
  }
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/{id}")
  void deleteCategory(@PathVariable Integer id) {
    categoryRepository.deleteById(id);
  }
  
  @GetMapping("/")
  public ResponseEntity<List<Category>> getAllCategoriesByUserId() {
    //extract email for Security Context
    String currentEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if("".equals(currentEmail)){
      Optional<User> devUser = userRepository.findByEmail("john.doe@example.com");
      if(devUser.isEmpty()){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
      }
      return ResponseEntity.ok(categoryRepository.findByUserId(devUser.get().id()));
    } else{
      return userRepository.findByEmail(currentEmail)
                 .map(user -> {
                   List<Category> categories = categoryRepository.findByUserId(user.id());
                   return ResponseEntity.ok(categories);
                 })
                 .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }
  }
}
