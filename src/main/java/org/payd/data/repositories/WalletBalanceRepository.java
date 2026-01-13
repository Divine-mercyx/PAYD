package org.payd.data.repositories;

import org.payd.data.models.WalletBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WalletBalanceRepository extends JpaRepository<WalletBalance, Long> {
    Optional<WalletBalance> findByUserId(Long userId);
}
