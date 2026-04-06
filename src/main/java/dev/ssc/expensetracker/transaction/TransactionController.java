package dev.ssc.expensetracker.transaction;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping ("/api/transactions")
public class TransactionController {
  TransactionRepository transactionRepository;
  
  public TransactionController (TransactionRepository transactionRepository) {
    this.transactionRepository = transactionRepository;
  }
  
  @GetMapping
  public ResponseEntity<List<Transaction>> getAllTransactions() {
    return ResponseEntity.ok(transactionRepository.getTransactions());
  }
  
  @GetMapping ("/{id}")
  public ResponseEntity<Transaction> getTransactionById (@PathVariable Long id) {
    Optional<Transaction> t = transactionRepository.getTransactionById(id);
    if (t.isPresent()) {
      return ResponseEntity.ok(t.get());
    }else{
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }
  
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  void addTransaction (@Validated @RequestBody Transaction transaction) {
    transactionRepository.addTransaction(transaction);
  }
  
  
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PutMapping ("/{id}")
  void updateTransaction(@Validated @RequestBody Transaction transaction,  @PathVariable Long id) {
    transactionRepository.updateTransaction(transaction, id);
  }
  
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping ("/{id}")
  void deleteTransaction(@PathVariable Long id) {
    transactionRepository.removeTransaction(id);
  }
  
  
  
}
