package com.devon.building.repository;

import com.devon.building.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findByCustomer_IdAndCodeAndActiveTrue(Long customerId, String code);
}
