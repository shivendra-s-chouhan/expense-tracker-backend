package dev.ssc.expensetracker.category;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

@Component
@Order(3)
public class CategoryJsonDataLoader implements CommandLineRunner {
  private final CategoryRepository categoryRepository;
  private final ObjectMapper objectMapper;
  private final Logger logger = LoggerFactory.getLogger(CategoryJsonDataLoader.class);
  public CategoryJsonDataLoader(CategoryRepository categoryRepository, ObjectMapper objectMapper) {
    this.categoryRepository = categoryRepository;
    this.objectMapper = objectMapper;
  }
  
  @Override
  public void run(String... args) throws Exception {
    if(categoryRepository.count() > 0) {
      logger.info("Skipping loading of categories");
      return;
    }
    try(InputStream inputstream = getClass().getClassLoader().getResourceAsStream("data/categories.json")){
      if (inputstream == null) {
        throw new IllegalStateException("Could not find resource data/categories.json");
      }
      Category[] allCategories = objectMapper.readValue(inputstream, Category[].class);
      logger.info("Reading {} categories from categories.json", allCategories.length);
      for(Category category : allCategories) {
        logger.info("Loading category {} from categories.json", category.getId());
        category.setId(null); // Ensure the ID is null so that the database can auto-generate it
        Category savedCategory = categoryRepository.save(category);
        logger.info("Saved category {} to database", savedCategory.getId());
      }
    } catch(IOException e) {
      throw new RuntimeException("Failed to load categories from json file", e);
    }
  }
}
