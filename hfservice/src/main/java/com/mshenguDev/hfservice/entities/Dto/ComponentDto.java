package com.mshenguDev.hfservice.entities.Dto;

import com.mshenguDev.hfservice.entities.Compatibility;
import com.mshenguDev.hfservice.entities.Furniture_Category;
import com.fasterxml.jackson.annotation.JsonAlias;

public class ComponentDto {
   // private String type;
   private String description;
   private Double price;
   @JsonAlias("compatible")
   private Compatibility compatibility;
   private Furniture_Category category;
   // private byte[] image;

   public ComponentDto() {
   }

   public ComponentDto(String description, Double price) {
      // this.type = type ;
      this.description = description;
      this.price = price;
      // this.image = image;
   }

   // public String getType(){return type;}
   public String getDescription() {
      return description;
   }

   public Double getPrice() {
      return price;
   }

   public Compatibility getCompatibility() {
      return compatibility;
   }

   public Furniture_Category getCategory() {
      return category;
   }
   // public byte[] getImage(){return image;}

   // public void setType(String type){this.type = type;}
   public void setDescription(String description) {
      this.description = description;
   }

   public void setPrice(Double price) {
      this.price = price;
   }

   public void setCompatibility(Compatibility compatibility) {
      this.compatibility = compatibility;
   }

   public void setCategory(Furniture_Category category) {
      this.category = category;
   }
   // public void setImage(byte[] image){this.image = image;}
}
