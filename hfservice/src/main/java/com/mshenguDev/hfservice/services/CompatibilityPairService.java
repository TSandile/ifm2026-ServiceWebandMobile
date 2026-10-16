package com.mshenguDev.hfservice.services;

import com.mshenguDev.hfservice.entities.CompatibilityStatus;
import com.mshenguDev.hfservice.entities.CompatiblePair;
import com.mshenguDev.hfservice.entities.Component;

import java.util.List;

public interface CompatibilityPairService {
    String registerPair(Component componentA, Component componentB);
    List<CompatiblePair> getAllPairsByStatus(CompatibilityStatus status);
    List<Component> getAllPairs();
    CompatiblePair getPairById(Long id);
    String deletePair(Long id);
}
