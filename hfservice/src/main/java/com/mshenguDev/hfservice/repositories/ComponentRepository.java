package com.mshenguDev.hfservice.repositories;

import com.mshenguDev.hfservice.entities.Component;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComponentRepository extends JpaRepository<Component, Long> {
    Optional<Component> findByType(String type);
}
