package com.example.voomacrud.util.specifications;

import com.example.voomacrud.entity.Account;
import com.example.voomacrud.entity.Card;
import com.example.voomacrud.enums.CardType;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class AccountSpecification {
    private AccountSpecification() {}

    // First request parameter filter: Get accounts with iban like a specific string

    public static Specification<Account> ibanLike(String ibanLike) {
        return ((root, query, builder) ->  builder.like(builder.lower(root.get("iban")), "%" + ibanLike.toLowerCase() + "%"));
    }
    /*
        Second request parameter filter: Get accounts that have cards with a specific type.
        This will require us to first join the account and card tables (OneToMany), and then applying the filter.
        To do this One to Many join (one account has many cards), we need to use the Join criteria to accomplish it
     */
    public static Specification<Account> hasCardsWithType(String cardType) {
        return (root, query, builder) ->  {
            Join<Account, Card> accountCards = root.join("cards");
            return builder.equal(accountCards.get("cardType"), CardType.valueOf(cardType).getIntVal());
        };
    }

    // Third request parameter filter: Get accounts belonging to one of the specified customers
    public static Specification<Account> withCustomer(List<Integer> customers) {
        return (root, query, builder) -> root.get("customerId")
                .in(customers);
    }
}
