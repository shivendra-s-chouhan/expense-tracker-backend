package dev.ssc.expensetracker.user;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

@Table ("users")
public record User(
    @Id
    Integer id,
    String name,
    @NotNull
    @Column("user_name")
    String userName,
    String email,
    String  password,
    @Embedded.Nullable(prefix = "")
    Address address,
    String phone,
    String website,
    @Embedded.Nullable(prefix = "company_")
    Company company
) {

}
