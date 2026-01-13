package org.payd.data.models;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "wallet_balances")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
public class WalletBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, precision = 18, scale = 8)
    private BigDecimal stablecoinBalance; // Current USDC/USDT available to spend

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal nairaEscrowBalance; // Naira held during the Flutterwave payout process

    private String currency; // Default "USDC"

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PreUpdate
    @PrePersist
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
