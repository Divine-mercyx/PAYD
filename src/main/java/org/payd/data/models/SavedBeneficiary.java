package org.payd.data.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "saved_beneficiaries")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SavedBeneficiary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String nickname; // e.g., "My Home Meter" or "Mom's GTB Account"

    private String beneficiaryType; // "BANK_ACCOUNT" or "UTILITY_BILLER"

    private String accountNumber; // 10-digit NUBAN or Meter Number

    private String bankCode; // e.g., "058" for GTB (Required for Flutterwave)

    private String billerType; // e.g., "IKEDC", "DSTV", "MTN_DATA"
}
