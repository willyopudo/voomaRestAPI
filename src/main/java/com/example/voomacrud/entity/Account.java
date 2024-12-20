package com.example.voomacrud.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String iban;

    @Column(nullable = false)
    private String bankCode;

    @Column(nullable = false)
    private int customerId;

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    private List<Card> cards;
}
