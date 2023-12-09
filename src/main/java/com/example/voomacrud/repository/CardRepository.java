package com.example.voomacrud.repository;

import com.example.voomacrud.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

	List<Card> findAllByAccountId(Long accountId);
}
