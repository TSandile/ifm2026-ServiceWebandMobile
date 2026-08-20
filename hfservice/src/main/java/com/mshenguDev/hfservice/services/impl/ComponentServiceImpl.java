package com.mshenguDev.hfservice.services.impl;

import com.mshenguDev.hfservice.entities.Component;
import com.mshenguDev.hfservice.entities.Dto.ComponentDto;
import com.mshenguDev.hfservice.repositories.ComponentRepository;
import com.mshenguDev.hfservice.services.ComponentService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class ComponentServiceImpl implements ComponentService {
    private final ComponentRepository componentRepository;

    public ComponentServiceImpl(ComponentRepository componentRepository){
        this.componentRepository = componentRepository;
    }

    @Override
    public String addComponent(ComponentDto componentDto, byte[] file) {
        if(componentDto.getType().isBlank() || componentDto.getDescription().isBlank() || componentDto.getPrice() == null){
            throw new NullPointerException("Entity fields are empty");
        }
        Component newComponent = new Component(componentDto.getType(), componentDto.getDescription(), componentDto.getPrice(), file);
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
}
