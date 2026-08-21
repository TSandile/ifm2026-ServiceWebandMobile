package com.mshenguDev.hfservice.services;

import com.mshenguDev.hfservice.entities.Component;
import com.mshenguDev.hfservice.entities.Dto.ComponentDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ComponentService{
    String addComponent(ComponentDto componentDto);
    Optional<Component> retrieveComponentById(Long id);
    Optional<Component> retrieveComponentByType(String type);
    List<Component> retrieveAllComponents();
    Long removeComponentById(Long id);
    String update(Long id, ComponentDto componentDto);
    String uploadImage(Long id, MultipartFile image) throws java.io.IOException;
}
