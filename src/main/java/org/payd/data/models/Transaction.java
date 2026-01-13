package org.payd.data.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String type; // e.g., "DEPOSIT", "BILL_PAY", "TRANSFER"

    @Column(precision = 18, scale = 8)
    private BigDecimal cryptoAmount; // Amount in USDC/USDT

    @Column(precision = 18, scale = 2)
    private BigDecimal fiatAmount; // Amount in NGN (Naira)

    private String status; // PENDING, COMPLETED, FAILED

    private String polygonTxHash; // The hash from the blockchain

    private String flutterwaveRef; // The reference from Flutterwave API

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
