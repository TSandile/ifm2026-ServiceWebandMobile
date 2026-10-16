package com.mshenguDev.hfservice.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;
    private String status;
    private Double totalAmount;
    private String bookingDate;
    private String notes;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public Order(){ }

    public Long getId() { return id; }
    public Long getCustomerId() { return customerId; }
    public String getStatus() { return status; }
    public Double getTotalAmount() { return totalAmount; }
    public String getBookingDate() { return bookingDate; }
    public String getNotes() { return notes; }
    public List<OrderItem> getItems() { return items; }

    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public void setStatus(String status) { this.status = status; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public void setBookingDate(String bookingDate) { this.bookingDate = bookingDate; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setItems(List<OrderItem> items) {
        this.items.clear();
        if(items != null){
            items.forEach(this::addItem);
        }
    }

    public void addItem(OrderItem item){
        item.setOrder(this);
        this.items.add(item);
    }
}
