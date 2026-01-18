package org.payd.data.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "kyc_documents",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "user_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KycDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IdType idType; // NIN, BVN, PASSPORT

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private KycStatus verificationStatus;

    @Column(nullable = false, unique = true)
    private String kycReferenceId;

    private LocalDateTime verifiedAt;
}
