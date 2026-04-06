package dev.ssc.expensetracker.account;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
public class AccountRepository {
  private List<Account> accounts = new ArrayList<>();

  List<Account> getAccounts() {return accounts;}

  @PostConstruct
  private void init() {
    accounts.add(new Account(
            1L,
            "Checking Account",
            5000.0
    ));

    accounts.add(new Account(
            2L,
            "Savings Account",
            10000.0
    ));
  }
  
  Optional<Account> getAccountById(Long id) {
    for (Account account : accounts) {
      if(account.getId().equals(id)) return Optional.of(account);
    }
    return Optional.empty();
  }
  
  void addAccount(Account account) {
    accounts.add(account);
  }
  
  void updateAccount(Account account, Long id) {
    Optional<Account> existingAccount = getAccountById(id);
    if(existingAccount.isPresent()) {
      account.setId(id);
      accounts.set(accounts.indexOf(existingAccount.get()), account);
    }
  }

  void removeAccount(Long id) {
    Optional<Account> existingAccount = getAccountById(id);
    if(existingAccount.isPresent()) {
      accounts.remove(existingAccount.get());
    }
  }
}

