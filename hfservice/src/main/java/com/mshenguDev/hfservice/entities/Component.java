package com.mshenguDev.hfservice.entities;

import jakarta.persistence.*;

@Entity
public class Component {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Enumerated(EnumType.STRING)
    private Furniture_Category category;
    @Enumerated(EnumType.STRING)
    private Compatibility compatibility;
    private String description;
    private Double price;

    @Enumerated(EnumType.STRING)
    private ComponentType type;

    @Enumerated(EnumType.STRING)
    private Material material;


    private Integer stock_level;
    private boolean in_stock;
    @Lob
    @Column(name ="image", columnDefinition = "LONGBLOB")
    private byte[] image;

    public Component(){}

    public Component( String description, Double price, byte[] image) {
        this.type = null;
        this.description = description;
        this.price = price;
        this.stock_level = 1;
        this.in_stock = true;
        this.image = image;

    }

    public Component( String description, Double price) {
        this.type = null;
        this.description = description;
        this.price = price;
        this.stock_level = 1;
        this.image = null;
        this.in_stock = true;
    }

    public Long getId(){return id;}
    public ComponentType getType(){return type;}
    public Compatibility getCompatibility(){return compatibility;}
    public Furniture_Category getCategory(){return category;}
    public Material getMaterial(){return material;}
    public String getDescription(){return description;}
    public Double getPrice(){return price;}
    public Integer getStock_level(){return stock_level;}
    public boolean isIn_stock(){return stock_level != null ? stock_level > 0 : in_stock;}
    public byte[] getImage(){return image;}

    public void setType(ComponentType type) {
        this.type = type;
    }
    public void setCategory(Furniture_Category category){this.category = category;}
    public void setCompatibility(Compatibility compatibility){this.compatibility = compatibility;}
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
        this.in_stock = stock_level != null && stock_level > 0;
    }
    public void setIn_stock(boolean in_stock){
        this.in_stock = in_stock;
    }
    public void setImage(byte[] image){
        this.image = image;
    }
}
