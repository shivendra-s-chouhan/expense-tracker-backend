package dev.ssc.expensetracker.category;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepository {
  private List<Category> categories = new ArrayList<>();
  List<Category> getCategories() {return categories;}
  @PostConstruct
  private void init() {
    categories.add(new Category(1L, "Food"));
    categories.add(new Category(2L, "Transportation"));
    categories.add(new Category(3L, "Entertainment"));
  }
  Optional<Category> getCategoryById(Long id) {
    for (Category category : categories) {
      if(category.getId().equals(id)) return Optional.of(category);
    }
    return Optional.empty();
  }
  void addCategory(Category category) {
    categories.add(category);
  }
  void updateCategory(Category category, Long id) {
    Optional<Category> existingCategory = getCategoryById(id);
    if(existingCategory.isPresent()) {
      category.setId(id);
      categories.set(categories.indexOf(existingCategory.get()), category);
    }
  }
  void removeCategory(Long id) {
    Optional<Category> existingCategory = getCategoryById(id);
    if(existingCategory.isPresent()) {
      categories.remove(existingCategory.get());
    }
  }
}
