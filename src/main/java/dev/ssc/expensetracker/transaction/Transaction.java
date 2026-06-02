package dev.ssc.expensetracker.transaction;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table("transactions")
public class Transaction {
    @Id
    private Integer id;

    @Column("user_id")
    @NotNull
    private Integer userId;

    @Positive
    private double amount;

    @NotNull
    private TransactionType type;

    @Column("account_id")
    private Integer accountId;

    @Column("category_id")
    private Integer categoryId;

    @NotNull
    private LocalDate date;

    public Transaction(Integer id, Integer userId, double amount, TransactionType type, Integer accountId, Integer categoryId, LocalDate date) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.type = type;
        this.accountId = accountId;
        this.categoryId = categoryId;
        this.date = date;
    }
    public Transaction() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
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
                ", userId=" + userId +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", accountId=" + accountId +
                ", categoryId=" + categoryId +
                ", date=" + date +
                '}';
    }
}
