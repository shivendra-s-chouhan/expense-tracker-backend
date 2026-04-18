package dev.ssc.expensetracker.user;

import org.springframework.data.relational.core.mapping.Embedded;

public record Address(
    String street,
    String suite,
    String city,
    String zipcode,
    @Embedded.Nullable(prefix = "geo_")
    Geo geo
) {
}
