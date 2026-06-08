package dev.ssc.expensetracker.user;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends ListCrudRepository<User, Integer> {
    @Query("SELECT * FROM users WHERE email = :email")
    Optional <User> findByEmail(@Param("email") String email);
    
    @Query("SELECT * FROM users WHERE user_name = :userName")
    Optional<User> findByUserName(@Param("userName") String userName);
}
