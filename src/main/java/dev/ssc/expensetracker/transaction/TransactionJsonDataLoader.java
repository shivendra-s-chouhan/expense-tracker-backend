package dev.ssc.expensetracker.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@Order(4)
public class TransactionJsonDataLoader implements CommandLineRunner {
  
  private static final Logger log = LoggerFactory.getLogger(TransactionJsonDataLoader.class);
  private final TransactionRepository transactionRepository;
  private final ObjectMapper objectMapper;
  
  public TransactionJsonDataLoader(TransactionRepository transactionRepository, ObjectMapper objectMapper) {
    this.transactionRepository = transactionRepository;
    this.objectMapper = objectMapper;
  }
  
  @Override
  public void run(String... args) throws Exception {
    if(transactionRepository.count() >  0){
      log.info("Skipping Json seed because transaction table already contains data");
      return;
    }
    try(InputStream inputStream = TransactionJsonDataLoader.class.getClassLoader().getResourceAsStream("data/transactions.json")){
      if (inputStream == null) {
        throw new IllegalStateException("Could not find resource data/transactions.json");
      }
      
      Transaction[] allTransactions = objectMapper.readValue(inputStream, Transaction[].class);
      log.info("Reading {} transactions from transactions.json", allTransactions.length);
      for(Transaction transaction : allTransactions) {
        log.info("Loading transaction {} from transactions.json", transaction.getId());
        transaction.setId(null); // Ensure the ID is null so that the database can auto-generate it
        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("Saved transaction {} to db", savedTransaction.getId());
      }
    } catch(Exception e){
      throw new RuntimeException("failed to load transactions from json file", e);
    }
  }
}
