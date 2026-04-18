package dev.ssc.expensetracker.account;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/accounts")
public class AccountController {
  AccountRepository accountRepository;
  public AccountController(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }
  @GetMapping
  public ResponseEntity<List<Account>> getAllAccounts() {
    return ResponseEntity.ok(accountRepository.findAll());
  }
  @GetMapping("/{id}")
  public ResponseEntity<Account> getAccountById(@PathVariable Integer id) {
    Optional<Account> account = accountRepository.findById(id);
    if (account.isPresent()) {
      return ResponseEntity.ok(account.get());
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  void addAccount(@Validated @RequestBody Account account) {
    accountRepository.save(account);
  }
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PutMapping("/{id}")
  void updateAccount(@Validated @RequestBody Account account, @PathVariable Integer id) {
    accountRepository.save(account);
  }
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/{id}")
  void deleteAccount(@PathVariable Integer id) {
    accountRepository.deleteById(id);
  }
  
  @GetMapping("/")
  public ResponseEntity<List<Account>> getAllAccountsByUserId(@RequestParam Integer userId) {
    return ResponseEntity.ok(accountRepository.findByUserId(userId));
  }
}
