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
  private final TransactionRepository transactionRepository;
  
  public TransactionController (TransactionRepository transactionRepository) {
    this.transactionRepository = transactionRepository;
  }
  
  @GetMapping
  public ResponseEntity<List<Transaction>> getAllTransactions() {
    return ResponseEntity.ok(transactionRepository.findAll());
  }
  
  @GetMapping ("/{id}")
  public ResponseEntity<Transaction> getTransactionById (@PathVariable Integer id) {
    Optional<Transaction> t = transactionRepository.findById(id);
    if (t.isPresent()) {
      return ResponseEntity.ok(t.get());
    }else{
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }
  
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  void addTransaction (@Validated @RequestBody Transaction transaction) {
    transactionRepository.save(transaction);
  }
  
  
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PutMapping ("/{id}")
  void updateTransaction(@Validated @RequestBody Transaction transaction,  @PathVariable Integer id) {
    transactionRepository.save(transaction);
  }
  
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping ("/{id}")
  void deleteTransaction(@PathVariable Integer id) {
    transactionRepository.deleteById(id);
  }
  
  @GetMapping("/")
  public ResponseEntity<List<Transaction>> getTransactionsByUserId (@RequestParam Integer userId) {
    return ResponseEntity.ok(transactionRepository.findByUserId(userId));
  }
  
}
