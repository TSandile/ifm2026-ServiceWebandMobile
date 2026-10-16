package com.mshenguDev.hfservice.repositories;

import com.mshenguDev.hfservice.entities.CompatibilityStatus;
import com.mshenguDev.hfservice.entities.CompatiblePair;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompatiblePairRepo extends JpaRepository<CompatiblePair, Long> {
    List<CompatiblePair> findByCompatibilityStatus(CompatibilityStatus compatibleStatus);
}
