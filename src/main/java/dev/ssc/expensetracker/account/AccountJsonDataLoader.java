package dev.ssc.expensetracker.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

@Component
@Order(2)
public class AccountJsonDataLoader implements CommandLineRunner {
  
  private final AccountRepository accountRepository;
  private final Logger log = LoggerFactory.getLogger(AccountJsonDataLoader.class);
  private final ObjectMapper objectMapper;
  
  public AccountJsonDataLoader(AccountRepository accountRepository, ObjectMapper objectMapper) {
    this.accountRepository = accountRepository;
    this.objectMapper = objectMapper;
  }
  
  @Override
  public void run(String... args) throws Exception {
    if (accountRepository.count() > 0) {
      log.info("Skipping loading of accounts");
      return;
    }

    try (InputStream inputStream = AccountJsonDataLoader.class.getClassLoader().getResourceAsStream("data/accounts.json")) {
      if (inputStream == null) {
        throw new IllegalStateException("Could not find resource data/accounts.json");
      }
      Account[] allAccounts = objectMapper.readValue(inputStream, Account[].class);
      log.info("Reading {} accounts from accounts.json", allAccounts.length);
      for (Account account : allAccounts) {
        log.info("Loading account {} from accounts.json", account.getId());
        account.setId(null); // Ensure the ID is null so that the database can auto-generate it
        Account savedAccount = accountRepository.save(account);
        log.info("Saved account {} to db", savedAccount.getId());
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to load accounts from accounts.json", e);
    }
  }
}
