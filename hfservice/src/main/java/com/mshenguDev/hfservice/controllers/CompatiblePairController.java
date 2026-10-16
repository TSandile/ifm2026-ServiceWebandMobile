package com.mshenguDev.hfservice.controllers;

import com.mshenguDev.hfservice.entities.CompatiblePair;
import com.mshenguDev.hfservice.services.CompatibilityPairService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/compatiblePairs")
@CrossOrigin
public class CompatiblePairController {

    private final CompatibilityPairService compatibilityPairService;

    public CompatiblePairController(CompatibilityPairService compatibilityPairService) {
        this.compatibilityPairService = compatibilityPairService;
    }

    public ResponseEntity<?> addCompatiblePair(@PathVariable Long componentA_Id,
                                               @PathVariable Long componentB_Id) {
        CompatiblePair compatiblePair = compatibilityPairService.addCompatiblePair(componentA_Id, componentB_Id);
        return ResponseEntity.ok(compatiblePair);
    }

    @GetMapping("/getCompatiblePair/{id}")
    public ResponseEntity<CompatiblePair> getCompatiblePairs(@PathVariable Long id) {
        return ResponseEntity.ok(compatibilityPairService.getPairById(id));
    }


}
