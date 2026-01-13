package org.payd.data.repositories;

import org.payd.data.models.KycDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface KycDocumentRepository extends JpaRepository<KycDocument, Long> {
    Optional<KycDocument> findByUserId(Long userId);
}

