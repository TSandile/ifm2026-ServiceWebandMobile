package com.mshenguDev.hfservice.entities.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderItemDto {
    @JsonProperty("component_id")
    private Long componentId;
    private String name;
    private String category;
    private Double price;
    private Integer quantity;

    public Long getComponentId() { return componentId; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public Double getPrice() { return price; }
    public Integer getQuantity() { return quantity; }

    public void setComponentId(Long componentId) { this.componentId = componentId; }
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setPrice(Double price) { this.price = price; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
