package dev.ssc.expensetracker.transaction;

import java.time.LocalDate;

public class TransactionResponseDTO {
  
  private Integer transaction_id;
  private Integer userId;
  private double amount;
  private TransactionType type;
  private String accountName;
  private String categoryName;
  private LocalDate date;

  public Integer getTransaction_id() {
    return transaction_id;
  }

  public void setTransaction_id(Integer transaction_id) {
    this.transaction_id = transaction_id;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public TransactionType getType() {
    return type;
  }

  public void setType(TransactionType type) {
    this.type = type;
  }

  public String getAccountName() {
    return accountName;
  }

  public void setAccountName(String accountName) {
    this.accountName = accountName;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }
}
