package com.mshenguDev.hfservice.entities.Dto;

public class InventoryReportItemDto {
    private Long id;
    private String category;
    private String compatibility;
    private String description;
    private Double price;
    private Integer stockLevel;
    private boolean inStock;

    public InventoryReportItemDto() {
    }

    public InventoryReportItemDto(Long id, String category, String compatibility, String description,
                                 Double price, Integer stockLevel, boolean inStock) {
        this.id = id;
        this.category = category;
        this.compatibility = compatibility;
        this.description = description;
        this.price = price;
        this.stockLevel = stockLevel;
        this.inStock = inStock;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCompatibility() {
        return compatibility;
    }

    public void setCompatibility(String compatibility) {
        this.compatibility = compatibility;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(Integer stockLevel) {
        this.stockLevel = stockLevel;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }
}
