package org.payd.data.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "kyc_documents")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class KycDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String idType; // "NIN", "BVN", "PASSPORT"
    private String idNumber; // Encrypted string
    private String verificationStatus; // "VERIFIED", "FAILED", "RETRY_REQUIRED"
    private String providerReference; // The ID from SmileID/YouVerify
    private String documentImageUrl; // Link to Supabase Storage (if applicable)

    private LocalDateTime verifiedAt;
}
