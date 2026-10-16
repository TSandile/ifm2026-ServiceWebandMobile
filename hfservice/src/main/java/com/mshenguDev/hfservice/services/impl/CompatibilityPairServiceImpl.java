package com.mshenguDev.hfservice.services.impl;

import com.mshenguDev.hfservice.entities.CompatibilityStatus;
import com.mshenguDev.hfservice.entities.CompatiblePair;
import com.mshenguDev.hfservice.entities.Component;
import com.mshenguDev.hfservice.repositories.CompatiblePairRepo;
import com.mshenguDev.hfservice.repositories.ComponentRepository;
import com.mshenguDev.hfservice.services.CompatibilityPairService;

import java.util.List;

public class CompatibilityPairServiceImpl implements CompatibilityPairService {
    private final CompatiblePairRepo  compatiblePairRepo;
    private final ComponentRepository componentRepository;
    public CompatibilityPairServiceImpl(CompatiblePairRepo compatiblePairRepo, ComponentRepository componentRepository) {
        this.compatiblePairRepo = compatiblePairRepo;
        this.componentRepository = componentRepository;
    }
    @Override
    public String registerPair(Long componentAId, Long componentBId) {
        if(componentAId == null || componentBId == null) {
            return "FAIL NO_ID";
        }

        Component componentA = componentRepository.findById(componentAId).orElse(null);
        Component componentB = componentRepository.findById(componentBId).orElse(null);

        if(componentA == null || componentB == null) {
            return "FAIL MISSING_COMPONENT";
        }

        CompatiblePair compatiblePair = new CompatiblePair();
        compatiblePair.setComponentA(componentA);
        compatiblePair.setComponentB(componentB);
        compatiblePair.setStatus(CompatibilityStatus.COMPATIBLE);
        compatiblePairRepo.save(compatiblePair);
        return "SUCCESS";
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

    @Override
    public String updateStatus(Long id, CompatibilityStatus status) {
        CompatiblePair existingCompPair = compatiblePairRepo.findById(id).orElse(null);
        if(existingCompPair != null){
            existingCompPair.setStatus(status);
            compatiblePairRepo.save(existingCompPair);
            return "SUCCESS";
        }
        return "FAIL";
    }
}
