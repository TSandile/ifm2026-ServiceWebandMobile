package com.mshenguDev.hfservice.controllers;

import com.mshenguDev.hfservice.entities.Component;
import com.mshenguDev.hfservice.entities.Dto.ComponentDto;
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
    public ComponentController(ComponentService componentService){
        this.componentService = componentService;
    }

    @PostMapping(path="/addComponent")
    public ResponseEntity<?> addComponent(@RequestBody ComponentDto componentDto){
        String Response = componentService.addComponent(componentDto);
        if(!Response.equals("SUCCESS")){
            return ResponseEntity.status(500).body("Failed to add component");
        }
        return ResponseEntity.ok("Component added successfully");
    }

    @PostMapping( "/uploadImage" )
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
    public ResponseEntity<?> updateComponent(@PathVariable Long id, @RequestBody ComponentDto componentDto) {
        String response = componentService.update(id, componentDto);
        if (!"SUCCESS".equals(response)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok("Component updated successfully");
    }

    @GetMapping("/getAllComponents")
    public ResponseEntity<List<Component>> getAllComponents(){
        List<Component> components = componentService.retrieveAllComponents();
        return ResponseEntity.ok(components);
    }

    @GetMapping("/getImage/{id}")
    public ResponseEntity<byte[]> getComponentImage(@PathVariable Long id){
        Component component = componentService.retrieveComponentById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No image found"));
        if(component.getImage() == null || component.getImage().length == 0){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No image found");
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(component.getImage());
    }
}
