package com.mshenguDev.hfservice.services.impl;

import com.mshenguDev.hfservice.entities.Component;
import com.mshenguDev.hfservice.entities.Dto.ComponentDto;
import com.mshenguDev.hfservice.repositories.ComponentRepository;
import com.mshenguDev.hfservice.services.ComponentService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ComponentServiceImpl implements ComponentService {
    private final ComponentRepository componentRepository;

    public ComponentServiceImpl(ComponentRepository componentRepository){
        this.componentRepository = componentRepository;
    }

    @Override
    public String addComponent(ComponentDto componentDto) {
        if(componentDto.getType().isBlank() || componentDto.getDescription().isBlank() || componentDto.getPrice() == null){
            throw new NullPointerException("Entity fields are empty");
        }
        Component newComponent = new Component(componentDto.getType(), componentDto.getDescription(), componentDto.getPrice());
        componentRepository.save(newComponent);
        return "SUCCESS";
    }

    @Override
    public Optional<Component> retrieveComponentById(Long id) {
        return componentRepository.findById(id);
    }

    @Override
    public Optional<Component> retrieveComponentByType(String type) {
        return componentRepository.findByType(type);
    }


    @Override
    public List<Component> retrieveAllComponents() {
        return componentRepository.findAll();
    }

    @Override
    public Long removeComponentById(Long id) {
        if(componentRepository.existsById(id)){
            componentRepository.deleteById(id);
            return id;
        }else{
            return null;
        }
    }

    @Override
    public String uploadImage(Long id, MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Image file is empty");
        }

        if (image.getSize() > 5L * 1024 * 1024) {
            throw new IllegalArgumentException("Image file is too large. Max size is 5MB");
        }

        if (image.getContentType() == null || !image.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }

        Component comp = componentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Component not found"));

        comp.setImage(image.getBytes());
        componentRepository.save(comp);
        return "SUCCESS";
    }
}
