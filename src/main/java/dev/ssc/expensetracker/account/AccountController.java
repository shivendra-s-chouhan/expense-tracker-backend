package dev.ssc.expensetracker.account;


import dev.ssc.expensetracker.user.User;
import dev.ssc.expensetracker.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/accounts")
public class AccountController {
  AccountRepository accountRepository;
  UserRepository userRepository;
  public AccountController(AccountRepository accountRepository, UserRepository userRepository) {
    this.accountRepository = accountRepository;
    this.userRepository = userRepository;
  }
  
  /* can be use for testing

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
     */

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
  public ResponseEntity<List<Account>> getAllAccountsByUserId() {
    // extract principal string from SecurityContextHolder \
    String currentEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if("".equals(currentEmail)){
      Optional<User> devUser = userRepository.findByEmail("john.doe@example.com");
      if(devUser.isEmpty()){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
      }
      return ResponseEntity.ok(accountRepository.findByUserId(devUser.get().id()));
    } else {
      return userRepository.findByEmail(currentEmail)
                 .map(user -> {
                   List<Account> userAccounts = accountRepository.findByUserId(user.id());
                   return ResponseEntity.ok(userAccounts);
                 })
                 .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }
  }
}
