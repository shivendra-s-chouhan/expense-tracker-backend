package dev.ssc.expensetracker.account;

import jakarta.annotation.PostConstruct;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



public interface AccountRepository extends ListCrudRepository<Account,Integer> {
    @Query("SELECT * FROM accounts WHERE user_id = :userId")
    List<Account> findByUserId(Integer userId);
}

