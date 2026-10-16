package com.mshenguDev.hfservice.entities.Dto;

import com.mshenguDev.hfservice.entities.ComponentCompatibility;
import com.mshenguDev.hfservice.entities.ComponentType;
import com.mshenguDev.hfservice.entities.ComponentCategory;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.mshenguDev.hfservice.entities.ComponentMaterial;

public class ComponentDto {
   // private String type;
   private String description;
   private Double price;
   @JsonAlias("compatible")
   private ComponentCompatibility compatibility;
   @JsonAlias("category")
   private ComponentCategory category;
   @JsonAlias("type")
   private ComponentType type;
   @JsonAlias("material")
   private ComponentMaterial material;

   public ComponentDto(){}

   public ComponentDto(  ComponentCategory category, ComponentCompatibility compatibility, String description, Double price,  ComponentType type, ComponentMaterial material){
      this.category = category;
      this.compatibility = compatibility;
       this.description = description;
       this.price = price;
       this.type = type;
       this.material = material;
   }

   //Mutators
   public void setCategory(ComponentCategory category){this.category = category;}
   public void setCompatibility(ComponentCompatibility compatibility){this.compatibility = compatibility;}
   public void setDescription(String description){this.description = description;}
   public void setPrice(Double price){this.price = price;}
   public void setType(ComponentType type){this.type = type;}
   public void setMaterial(ComponentMaterial material){this.material = material;}

   //accessors
   public ComponentCategory getCategory(){return category;}
   public ComponentCompatibility getCompatibility(){return compatibility;}
   public String getDescription(){return description;}
   public Double getPrice(){return price;}
   public ComponentType getType(){return type;}
   public ComponentMaterial getMaterial(){return material;}

}
