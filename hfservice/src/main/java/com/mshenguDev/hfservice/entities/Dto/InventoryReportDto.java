package com.mshenguDev.hfservice.entities.Dto;

import java.util.List;
import java.util.Map;

public class InventoryReportDto {
    private long totalComponents;
    private long inStockCount;
    private long outOfStockCount;
    private long lowStockCount;
    private double inventoryValue;
    private List<InventoryReportItemDto> items;
    private Map<String, Long> categoryBreakdown;

    public InventoryReportDto() {
    }

    public InventoryReportDto(long totalComponents, long inStockCount, long outOfStockCount,
                             long lowStockCount, double inventoryValue,
                             List<InventoryReportItemDto> items, Map<String, Long> categoryBreakdown) {
        this.totalComponents = totalComponents;
        this.inStockCount = inStockCount;
        this.outOfStockCount = outOfStockCount;
        this.lowStockCount = lowStockCount;
        this.inventoryValue = inventoryValue;
        this.items = items;
        this.categoryBreakdown = categoryBreakdown;
    }

    public long getTotalComponents() {
        return totalComponents;
    }

    public void setTotalComponents(long totalComponents) {
        this.totalComponents = totalComponents;
    }

    public long getInStockCount() {
        return inStockCount;
    }

    public void setInStockCount(long inStockCount) {
        this.inStockCount = inStockCount;
    }

    public long getOutOfStockCount() {
        return outOfStockCount;
    }

    public void setOutOfStockCount(long outOfStockCount) {
        this.outOfStockCount = outOfStockCount;
    }

    public long getLowStockCount() {
        return lowStockCount;
    }

    public void setLowStockCount(long lowStockCount) {
        this.lowStockCount = lowStockCount;
    }

    public double getInventoryValue() {
        return inventoryValue;
    }

    public void setInventoryValue(double inventoryValue) {
        this.inventoryValue = inventoryValue;
    }

    public List<InventoryReportItemDto> getItems() {
        return items;
    }

    public void setItems(List<InventoryReportItemDto> items) {
        this.items = items;
    }

    public Map<String, Long> getCategoryBreakdown() {
        return categoryBreakdown;
    }

    public void setCategoryBreakdown(Map<String, Long> categoryBreakdown) {
        this.categoryBreakdown = categoryBreakdown;
    }
}
