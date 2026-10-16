package com.mshenguDev.hfservice.services.impl;

import com.mshenguDev.hfservice.entities.CompatibilityStatus;
import com.mshenguDev.hfservice.entities.CompatiblePair;
import com.mshenguDev.hfservice.entities.Component;
import com.mshenguDev.hfservice.repositories.CompatiblePairRepo;
import com.mshenguDev.hfservice.services.CompatibilityPairService;

import java.util.List;

public class CompatibilityPairServiceImpl implements CompatibilityPairService {
    private final CompatiblePairRepo  compatiblePairRepo;
    public CompatibilityPairServiceImpl(CompatiblePairRepo compatiblePairRepo) {
        this.compatiblePairRepo = compatiblePairRepo;
    }
    @Override
    public String registerPair(Component componentA, Component componentB) {
        CompatiblePair compatiblePair = new CompatiblePair(componentA, componentB);

        return "";
    }

    @Override
    public List<CompatiblePair> getAllPairsByStatus(CompatibilityStatus status) {
        return compatiblePairRepo.findByCompatibilityStatus(status);
    }

    @Override
    public List<Component> getAllPairs() {
        return List.of();
    }

    @Override
    public CompatiblePair getPairById(Long id) {
        return compatiblePairRepo.findById(id).orElse(null);
    }

    @Override
    public String deletePair(Long id) {
        if(compatiblePairRepo.existsById(id)){
            compatiblePairRepo.deleteById(id);
            return "SUCCESS";
        }else{
        return "FAIL";}
    }
}
