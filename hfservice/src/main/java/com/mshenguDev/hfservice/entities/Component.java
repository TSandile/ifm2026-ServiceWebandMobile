package com.mshenguDev.hfservice.entities;

import jakarta.persistence.*;

@Entity
public class Component {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String description;
    private Double price;

    @Enumerated(EnumType.STRING)
    private ComponentType type;

    @Enumerated(EnumType.STRING)
    private Material material;


    public static Integer stock_level = 0;
    @Lob
    @Column(name ="image", columnDefinition = "LONGBLOB")
    private byte[] image;

    public Component(){}

    public Component( String description, Double price, byte[] image) {
        this.type = null;
        this.description = description;
        this.price = price;
        stock_level += 1;
        this.image = image;

    }

    public Component( String description, Double price) {
        this.type = null;
        this.description = description;
        this.price = price;
        stock_level += 1;
        this.image = null;
    }

    public Long getId(){return id;}
    public ComponentType getType(){return type;}
    public Material getMaterial(){return material;}
    public String getDescription(){return description;}
    public Double getPrice(){return price;}
    public Integer getStock_level(){return stock_level;}
    public byte[] getImage(){return image;}

    public void setType(ComponentType type) {
        this.type = type;
    }

    public void setMaterial(Material material){
        this.material = material;
    }

    public void setDescription(String description){
        this.description = description;
    }
    public void setPrice(Double price){
        this.price = price;
    }
    public void setStock_level(Integer stock_level){
        this.stock_level = stock_level;
    }
    public void setImage(byte[] image){
        this.image = image;
    }
}
