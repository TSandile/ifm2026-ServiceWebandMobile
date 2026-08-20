package com.mshenguDev.hfservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Component {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String description;
    private Double price;
    private Integer stock_level;

}
