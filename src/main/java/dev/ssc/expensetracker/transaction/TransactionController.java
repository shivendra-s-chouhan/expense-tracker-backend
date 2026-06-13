package dev.ssc.expensetracker.transaction;

import dev.ssc.expensetracker.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
  private final UserRepository userRepository;
  
  public TransactionController (TransactionRepository transactionRepository, UserRepository userRepository) {
    this.transactionRepository = transactionRepository;
    this.userRepository = userRepository;
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
  
  @GetMapping("/transactionsbyuserid")
  public ResponseEntity<List<Transaction>> getTransactionsByUserId (@RequestParam Integer userId) {
    return ResponseEntity.ok(transactionRepository.findByUserId(userId));
  }
  @GetMapping("/")
  public ResponseEntity<List<TransactionResponseDTO>> findAllTransactionDetailsWithAccountAndCategoryName   () {
    // extract principal string from SecurityContextHolder
    // if security is on it will have the actual email
    // if security is off it will have empty string "" and hardcoded to john.doe@example.com
    String currentEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if("".equals(currentEmail)){
      Optional<dev.ssc.expensetracker.user.User> devUser = userRepository.findByEmail("john.doe@example.com");
      if(devUser.isEmpty()){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
      }
      List<TransactionResponseDTO> devTxList = transactionRepository.findAllTransactionDetailsWithAccountAndCategoryNameByUserId(devUser.get().id());
      return ResponseEntity.ok(devTxList);
      
    } else {
          return userRepository.findByEmail(currentEmail)
                     .map(user -> {
                       List<TransactionResponseDTO> userTransactions = transactionRepository.findAllTransactionDetailsWithAccountAndCategoryNameByUserId(user.id());
                       return ResponseEntity.ok(userTransactions);
                     })
                     .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }
  }
  
}
