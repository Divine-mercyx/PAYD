package org.payd.data.repositories;

import org.payd.data.models.SavedBeneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SavedBeneficiaryRepository extends JpaRepository<SavedBeneficiary, Long> {
    List<SavedBeneficiary> findByUserId(Long userId);
    List<SavedBeneficiary> findByUserIdAndBeneficiaryType(Long userId, String type);
}
