package com.mshenguDev.hfservice.entities;

import jakarta.persistence.*;

@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long componentId;
    private String name;
    private String category;
    private Double price;
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    public OrderItem(){}

    public Long getId() { return id; }
    public Long getComponentId() { return componentId; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public Double getPrice() { return price; }
    public Integer getQuantity() { return quantity; }
    public Order getOrder() { return order; }

    public void setComponentId(Long componentId) { this.componentId = componentId; }
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setPrice(Double price) { this.price = price; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public void setOrder(Order order) { this.order = order; }
}
