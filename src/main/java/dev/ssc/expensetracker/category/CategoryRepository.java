package dev.ssc.expensetracker.category;
import jakarta.annotation.PostConstruct;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public interface CategoryRepository extends ListCrudRepository<Category, Integer> {
    @Query("SELECT * FROM categories WHERE user_id = :userId")
    List<Category> findByUserId(Integer userId);
}
