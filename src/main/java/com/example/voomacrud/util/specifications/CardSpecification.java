package com.example.voomacrud.util.specifications;

import com.example.voomacrud.entity.Account;
import com.example.voomacrud.entity.Card;
import com.example.voomacrud.enums.CardType;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class CardSpecification {
    private CardSpecification() {}

    public static Specification<Card> withType(String cardType) {
        return ((root, query, builder) ->  builder.equal(root.get("cardType"), CardType.valueOf(cardType).getIntVal()));
    }

    public static Specification<Card> withAccountId(List<String> accounts) {
        return (root, query, builder) ->  {
            Join<Card, Account> accountCards = root.join("account");
            return accountCards.get("iban").in(accounts);
        };
    }

}
