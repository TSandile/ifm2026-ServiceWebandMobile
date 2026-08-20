package com.mshenguDev.hfservice.imageManager.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ImageData")
public class ImageData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;

    @Lob
    @Column(name = "image_data", length = 1000)
    private byte[] imageData;

    public ImageData(){}
    public ImageData(String name, String type){
        this.name = name;
        this.type = type;
    }

    public void setName(String name){ this.name = name;}
    public void setType(String type){this.type = type;}

    public String getName(){return name;}
    public String getType(){return type;}
}
