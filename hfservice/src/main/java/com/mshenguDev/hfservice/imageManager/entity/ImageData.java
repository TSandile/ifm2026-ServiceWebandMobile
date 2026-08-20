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

    public ImageData(Builder builder){
        this.name = builder.name;
        this.type = builder.type;
        this.imageData = builder.imageData;
    }

    public static Builder builder(){
        return new Builder();
    }


    public void setName(String name){ this.name = name;}
    public void setType(String type){this.type = type;}

    public Long getId(){return id;}
    public String getName(){return name;}
    public String getType(){return type;}
    public byte[] getImageData(){return imageData;}

    public static final class Builder{
        private String name;
        private String type;
        private byte[] imageData;

        private Builder(){}
        public Builder name(String name){
            this.name = name;
            return this;
        }
        public Builder type(String type){
            this.type = type;
            return this;
        }
        public Builder imageData(byte[] imageData){
            this.imageData = imageData;
            return this;
        }
        public ImageData build(){
            return new ImageData(this);
        }

    }
}
