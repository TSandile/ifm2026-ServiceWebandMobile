package com.mshenguDev.hfservice.controllers;

import com.mshenguDev.hfservice.entities.*;
import com.mshenguDev.hfservice.entities.Dto.ComponentDto;
import com.mshenguDev.hfservice.entities.Dto.InventoryReportDto;
import com.mshenguDev.hfservice.entities.Dto.StockLevelDto;
import com.mshenguDev.hfservice.services.ComponentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/api/components")
@CrossOrigin
public class ComponentController {
    private final ComponentService componentService;

    public ComponentController(ComponentService componentService) {
        this.componentService = componentService;
    }

//    @PostMapping(path = "/addComponent")
//    public ResponseEntity<?> addComponent(@RequestBody ComponentDto componentDto) {
//        String Response = componentService.addComponent(componentDto);
//        if (!Response.equals("SUCCESS")) {
//            return ResponseEntity.status(500).body("Failed to add component");
//        }
//        return ResponseEntity.ok("Component added successfully");
//    }

    @PostMapping("/registerComponent")
    public ResponseEntity<?> registerComponent(
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "compatibility", required = true) String compatibility,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "material", required = true) String material,
            @RequestParam("image") MultipartFile image) {
        ComponentDto componentDto = new ComponentDto();
//
        componentDto.setCategory(ComponentCategory.valueOf(category.trim().toUpperCase()));
        componentDto.setCompatibility(ComponentCompatibility.valueOf(compatibility.trim().toUpperCase()));
        componentDto.setDescription(description);
        componentDto.setPrice(price);
        componentDto.setType(ComponentType.valueOf(type.trim().toUpperCase()));
        componentDto.setMaterial(ComponentMaterial.valueOf(material.trim().toUpperCase()));

        try {
            String response = componentService.registerComponent(componentDto, image);
            if (!"SUCCESS".equals(response)) {
                return ResponseEntity.badRequest().body(response);
            }
            return ResponseEntity.ok("Component registered successfully");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Unable to register component: " + e.getMessage());
        }
    }

    @PostMapping("/uploadImage")
    public ResponseEntity<?> uploadImage(@RequestParam("id") Long id, @RequestParam("image") MultipartFile image) {
        if (image == null || image.isEmpty()) {
            return ResponseEntity.badRequest().body("Image file is required");
        }

        try {
            String response = componentService.uploadImage(id, image);
            if (!"SUCCESS".equals(response)) {
                return ResponseEntity.badRequest().body(response);
            }
            return ResponseEntity.ok("Image uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Unable to upload image: " + e.getMessage());
        }
    }

    @PutMapping("/updateComponent/{id}")
    public ResponseEntity<?> updateComponent(@PathVariable Long id,
                                             @RequestParam("description") String description,
                                             @RequestParam("price") Double price,
                                             @RequestParam(value = "category", required = true) String category,
                                             @RequestParam(value = "compatibility", required = true) String compatibility,
                                             @RequestParam(value = "type", required = true) String type,
                                             @RequestParam(value = "material", required = true) String material,
                                             @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
        ComponentDto componentDto = new ComponentDto();
        componentDto.setDescription(description);
        componentDto.setPrice(price);
        componentDto.setCategory(ComponentCategory.valueOf(category.trim().toUpperCase()));
        componentDto.setCompatibility(ComponentCompatibility.valueOf(compatibility.trim().toUpperCase()));
        componentDto.setType(ComponentType.valueOf(type.trim().toUpperCase()));
        componentDto.setMaterial(ComponentMaterial.valueOf(material.trim().toUpperCase()));

        String response = componentService.update(id, componentDto, image);
        if (!"SUCCESS".equals(response)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok("Component updated successfully");
    }

    @GetMapping("/getAllComponents")
    public ResponseEntity<List<Component>> getAllComponents() {
        List<Component> components = componentService.retrieveAllComponents();
        return ResponseEntity.ok(components);
    }

    @GetMapping("/getImage/{id}")
    public ResponseEntity<byte[]> getComponentImage(@PathVariable Long id) {
        Component component = componentService.retrieveComponentById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No image found"));
        if (component.getImage() == null || component.getImage().length == 0) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No image found");
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(component.getImage());
    }

    @GetMapping("/getComponent/{id}")
    public ResponseEntity<Component> getSingleComponent(@PathVariable Long id) {
        Component component = componentService.retrieveComponentById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Component not found"));
        return ResponseEntity.ok(component);
    }

    @DeleteMapping("/deleteComponent/{id}")
    public ResponseEntity<?> deleteComponentById(@PathVariable Long id) {
        Long deletedId = componentService.removeComponentById(id);
        if (deletedId == null) {
            return ResponseEntity.status(500).body("Failed to delete component");
        }
        return ResponseEntity.ok("Component deleted successfully");
    }

    @PutMapping("/updateStockLevel/{id}/{stockLevel}")
    public ResponseEntity<?> updateStockLevel(
            @PathVariable Long id,
            @PathVariable Integer stockLevel) {
        if (stockLevel == null || stockLevel < 0) {
            return ResponseEntity.badRequest().body("Stock level must be 0 or greater");
        }

        String response = componentService.updateStockLevel(id, stockLevel);
        if (!"SUCCESS".equals(response)) {
            return ResponseEntity.status(500).body("Failed to update stock level");
        }
        return ResponseEntity.ok("Stock level updated successfully");
    }

    @GetMapping("/inventory-report")
    public ResponseEntity<InventoryReportDto> getInventoryReport() {
        return ResponseEntity.ok(componentService.buildInventoryReport());
    }
}
