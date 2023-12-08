package com.example.voomacrud.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cardAlias;

    @Column(nullable = false)
    private byte cardType;

    @OneToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account accountId;
}
