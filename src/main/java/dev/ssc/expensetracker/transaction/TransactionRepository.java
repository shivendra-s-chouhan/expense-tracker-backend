package dev.ssc.expensetracker.transaction;

import jakarta.annotation.PostConstruct;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public interface TransactionRepository extends ListCrudRepository<Transaction, Integer> {
      @Query("select * from transactions where user_id = :userId")
      List<Transaction> findByUserId(Integer userId);
      
      @Query("select t.id as transaction_id, t.user_id, t.amount, t.type, t.date, a.name as account_name, c.name as category_name from transactions t inner join accounts a on t.account_id = a.id inner join categories c on t.category_id = c.id where t.user_id = :userId")
      List<TransactionResponseDTO> findAllTransactionDetailsWithAccountAndCategoryNameByUserId(Integer userId);
}
