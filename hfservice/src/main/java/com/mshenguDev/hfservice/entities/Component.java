package com.mshenguDev.hfservice.entities;

import jakarta.persistence.*;

@Entity
public class Component {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String type;
    private String description;
    private Double price;

    private Integer stock_level = 0;
    @Lob
    @Column(name ="image")
    private byte[] image;

    public Component(){}

    public Component(String type, String description, Double price, byte[] image) {
        this.type = type;
        this.description = description;
        this.price = price;
        this.stock_level = 1;
        this.image = image;
    }

    public Long getId(){return id;}
    public String getType(){return type;}
    public String getDescription(){return description;}
    public Double getPrice(){return price;}
    public Integer getStock_level(){return stock_level;}
    public byte[] getImage(){return image;}

    public void setType(String type) {
        this.type = type;
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
