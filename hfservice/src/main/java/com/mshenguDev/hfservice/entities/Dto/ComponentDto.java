package com.mshenguDev.hfservice.entities.Dto;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;

public class ComponentDto {
    private String type;
    private String description;
    private Double price;
    private byte[] image;

    public ComponentDto(){}
    public ComponentDto(String type, String description,Double price, byte[] image){
        this.type = type ;
        this.description = description;
        this.price = price;
        this.image = image;
    }

    public String getType(){return type;}
    public String getDescription(){return description;}
    public Double getPrice(){return price;}
    public byte[] getImage(){return image;}

    public void setType(String type){this.type = type;}
    public void setDescription(String description){this.description = description;}
    public void setPrice(Double price){this.price = price;}
    public void setImage(byte[] image){this.image = image;}
}
