package org.payd.data.repositories;

import org.payd.data.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserId(Long userId);
    Transaction findByFlutterwaveRef(String flutterwaveRef);
}
