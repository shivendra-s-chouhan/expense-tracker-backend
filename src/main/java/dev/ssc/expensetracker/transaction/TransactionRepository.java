package dev.ssc.expensetracker.transaction;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TransactionRepository {
  private List<Transaction> transactions = new ArrayList<>();

  List<Transaction> getTransactions() {return  transactions;}

  @PostConstruct
  private void init() {
      transactions.add(new Transaction(
              1L,
              100.0,
              TransactionType.EXPENSE,
              1L,
              1L,
              LocalDate.now()
      ));

      transactions.add(new Transaction(
              2L,
              200.0,
              TransactionType.INCOME,
              1L,
              1L,
              LocalDate.now().plusDays(1)
      ));
  }
  
  Optional<Transaction> getTransactionById(Long id) {
      for (Transaction transaction : transactions) {
          if(transaction.getId().equals(id)) return Optional.of(transaction);
      }
      return Optional.empty();
  }
  
  void addTransaction(Transaction transaction) {
    transactions.add(transaction);
  }
  
  void updateTransaction(Transaction transaction, Long id) {
    Optional<Transaction> existingTransaction = getTransactionById(id);
    if(existingTransaction.isPresent()) {
        transactions.set(transactions.indexOf(existingTransaction.get()), transaction);
    }
  }
  
  void removeTransaction(Long id) {
  transactions.removeIf(transaction -> transaction.getId().equals(id));
  }
}
