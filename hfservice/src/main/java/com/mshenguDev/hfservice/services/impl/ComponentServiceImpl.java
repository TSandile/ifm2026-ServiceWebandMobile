package com.mshenguDev.hfservice.services.impl;

import com.mshenguDev.hfservice.entities.*;
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

    public ComponentServiceImpl(ComponentRepository componentRepository) {
        this.componentRepository = componentRepository;
    }

    @Override
    public String addComponent(ComponentDto componentDto) {
        if (componentDto.getDescription().isBlank() || componentDto.getPrice() == null) {
            throw new NullPointerException("Entity fields are empty");
        }
        Component newComponent = new Component(componentDto.getDescription(), componentDto.getPrice());
        newComponent.setCategory(componentDto.getCategory());
        newComponent.setCompatibility(componentDto.getCompatibility());
        if (componentDto.getDescription().toUpperCase().contains("TABLE")) {
            if (componentDto.getDescription().toUpperCase().contains("LEG")) {
                newComponent.setType(ComponentType.TABLE_LEG);
                if (componentDto.getDescription().toUpperCase().contains("WOOD")) {
                    newComponent.setMaterial(Material.WOOD);
                } else if (componentDto.getDescription().toUpperCase().contains("STEEL")) {
                    newComponent.setMaterial(Material.STEEL);
                }
            } else if (componentDto.getDescription().toUpperCase().contains("TOP")) {
                newComponent.setType(ComponentType.TABLE_TOP);
                if (componentDto.getDescription().toUpperCase().contains("WOOD")) {
                    newComponent.setMaterial(Material.WOOD);
                } else if (componentDto.getDescription().toUpperCase().contains("STEEL")) {
                    newComponent.setMaterial(Material.STEEL);
                }
            }

        } else if (componentDto.getDescription().toUpperCase().contains("CHAIR")) {
            {
                if (componentDto.getDescription().toUpperCase().contains("SEAT")) {
                    newComponent.setType(ComponentType.CHAIR_SEAT);
                    if (componentDto.getDescription().toUpperCase().contains("WOOD")) {
                        newComponent.setMaterial(Material.WOOD);
                    } else if (componentDto.getDescription().toUpperCase().contains("STEEL")) {
                        newComponent.setMaterial(Material.STEEL);
                    }
                } else if (componentDto.getDescription().toUpperCase().contains("LEG")) {
                    newComponent.setType(ComponentType.CHAIR_LEG);
                    if (componentDto.getDescription().toUpperCase().contains("WOOD")) {
                        newComponent.setMaterial(Material.WOOD);
                    } else if (componentDto.getDescription().toUpperCase().contains("STEEL")) {
                        newComponent.setMaterial(Material.STEEL);
                    }
                }
            }
        }
        componentRepository.save(newComponent);
        return "SUCCESS";
    }

    @Override
    public String registerComponent(ComponentDto componentDto, MultipartFile image) throws IOException {
        if (componentDto.getDescription().isBlank() || componentDto.getPrice() == null) {
            throw new NullPointerException("Entity fields are empty");
        }
        Component newComponent = new Component(componentDto.getDescription(), componentDto.getPrice());
        newComponent.setCategory(componentDto.getCategory());
        newComponent.setCompatibility(componentDto.getCompatibility());
        if (componentDto.getDescription().toUpperCase().contains("TABLE")) {
            if (componentDto.getDescription().toUpperCase().contains("LEG")) {
                newComponent.setType(ComponentType.TABLE_LEG);
                if (componentDto.getDescription().toUpperCase().contains("WOOD")) {
                    newComponent.setMaterial(Material.WOOD);
                } else if (componentDto.getDescription().toUpperCase().contains("STEEL")) {
                    newComponent.setMaterial(Material.STEEL);
                }
            } else if (componentDto.getDescription().toUpperCase().contains("TOP")) {
                newComponent.setType(ComponentType.TABLE_TOP);
                if (componentDto.getDescription().toUpperCase().contains("WOOD")) {
                    newComponent.setMaterial(Material.WOOD);
                } else if (componentDto.getDescription().toUpperCase().contains("STEEL")) {
                    newComponent.setMaterial(Material.STEEL);
                }
            }

        } else if (componentDto.getDescription().toUpperCase().contains("CHAIR")) {
            {
                if (componentDto.getDescription().toUpperCase().contains("SEAT")) {
                    newComponent.setType(ComponentType.CHAIR_SEAT);
                    if (componentDto.getDescription().toUpperCase().contains("WOOD")) {
                        newComponent.setMaterial(Material.WOOD);
                    } else if (componentDto.getDescription().toUpperCase().contains("STEEL")) {
                        newComponent.setMaterial(Material.STEEL);
                    }
                } else if (componentDto.getDescription().toUpperCase().contains("LEG")) {
                    newComponent.setType(ComponentType.CHAIR_LEG);
                    if (componentDto.getDescription().toUpperCase().contains("WOOD")) {
                        newComponent.setMaterial(Material.WOOD);
                    } else if (componentDto.getDescription().toUpperCase().contains("STEEL")) {
                        newComponent.setMaterial(Material.STEEL);
                    }
                }
            }
        }
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Image file is empty");
        }

        if (image.getSize() > 5L * 1024 * 1024) {
            throw new IllegalArgumentException("Image file is too large. Max size is 5MB");
        }

        if (image.getContentType() == null || !image.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }
        newComponent.setImage(image.getBytes());
        componentRepository.save(newComponent);
        return "SUCCESS";
    }

    @Override
    public String updateStockLevel(Long id, Integer stockLevel) {
        if (stockLevel == null || stockLevel < 0) {
            throw new IllegalArgumentException("Stock level must be 0 or greater");
        }

        Component component = componentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Component not found"));

        component.setStock_level(stockLevel);
        component.setIn_stock(stockLevel > 0);
        componentRepository.save(component);

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
    public Optional<Component> retrieveComponentByCategory(String category) {
        return componentRepository.findByCategory(Furniture_Category.valueOf(category.toUpperCase()));
    }

    @Override
    public Optional<Component> retrieveComponentByCompatibility(String compatibility) {
        return componentRepository.findByCompatibility(Compatibility.valueOf(compatibility.toUpperCase()));
    }

    @Override
    public List<Component> retrieveAllComponents() {
        return componentRepository.findAll();
    }

    @Override
    public Long removeComponentById(Long id) {
        if (componentRepository.existsById(id)) {
            componentRepository.deleteById(id);
            return id;
        } else {
            return null;
        }
    }

    @Override
    public String update(Long id, ComponentDto componentDto) {
        Component component = componentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Component not found"));
        // component.setType(componentDto.getType());
        component.setDescription(componentDto.getDescription());
        component.setPrice(componentDto.getPrice());
        component.setCategory(componentDto.getCategory());
        component.setCompatibility(componentDto.getCompatibility());
        componentRepository.save(component);

        return "SUCCESS";
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
