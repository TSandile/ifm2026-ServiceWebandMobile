package com.mshenguDev.hfservice.controllers;

import com.mshenguDev.hfservice.entities.Component;
import com.mshenguDev.hfservice.entities.Dto.ComponentDto;
import com.mshenguDev.hfservice.services.ComponentService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(path="/addComponent",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addComponent(@RequestBody ComponentDto componentDto, @RequestParam("image")MultipartFile file) throws IOException {
        String Response = componentService.addComponent(componentDto, file.getBytes());
        if(!Response.equals("success")){
            return ResponseEntity.status(500).body("Failed to add component");
        }
        return ResponseEntity.ok("Component added successfully");
    }
    @GetMapping("/getAllComponents")
    public ResponseEntity<List<Component>> getAllComponents(){
        List<Component> components = componentService.retrieveAllComponents();
        return ResponseEntity.ok(components);
    }
}
