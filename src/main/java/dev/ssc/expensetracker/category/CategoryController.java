package dev.ssc.expensetracker.category;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/categories")
public class CategoryController {
  CategoryRepository categoryRepository;
  public CategoryController(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }
  @GetMapping
  public ResponseEntity<List<Category>> getAllCategories() {
    return ResponseEntity.ok(categoryRepository.getCategories());
  }
  @GetMapping("/{id}")
  public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
    Optional<Category> category = categoryRepository.getCategoryById(id);
    if (category.isPresent()) {
      return ResponseEntity.ok(category.get());
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  void addCategory(@Validated @RequestBody Category category) {
    categoryRepository.addCategory(category);
  }
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PutMapping("/{id}")
  void updateCategory(@Validated @RequestBody Category category, @PathVariable Long id) {
    categoryRepository.updateCategory(category, id);
  }
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/{id}")
  void deleteCategory(@PathVariable Long id) {
    categoryRepository.removeCategory(id);
  }
}
