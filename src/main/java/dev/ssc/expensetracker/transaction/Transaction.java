package dev.ssc.expensetracker.transaction;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class Transaction {
    private Long id;
    @Positive
    private double amount;
    @NotNull
    private TransactionType type;
    private Long accountId;
    private Long categoryId;
    @NotNull
    private LocalDate date;

    public Transaction(Long id, double amount, TransactionType type, Long accountId, Long categoryId, LocalDate date) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.accountId = accountId;
        this.categoryId = categoryId;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", accountId=" + accountId +
                ", categoryId=" + categoryId +
                ", date=" + date +
                '}';
    }
}
