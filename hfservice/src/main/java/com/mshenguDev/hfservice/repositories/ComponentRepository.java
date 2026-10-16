package com.mshenguDev.hfservice.repositories;

import com.mshenguDev.hfservice.entities.ComponentCompatibility;
import com.mshenguDev.hfservice.entities.Component;
import com.mshenguDev.hfservice.entities.ComponentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComponentRepository extends JpaRepository<Component, Long> {
    Optional<Component> findByType(String type);
    Optional<Component> findByCategory(ComponentCategory category);
    Optional<Component> findByCompatibility(ComponentCompatibility compatibility);
}
