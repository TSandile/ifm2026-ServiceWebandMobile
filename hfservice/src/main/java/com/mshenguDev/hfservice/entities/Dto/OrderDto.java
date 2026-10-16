package com.mshenguDev.hfservice.entities.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class OrderDto {
    @JsonProperty("customer_id")
    private Long customerId;
    private String status;

    @JsonProperty("total_amount")
    private Double totalAmount;

    @JsonProperty("booking_date")
    private String bookingDate;
    private String notes;
    private List<OrderItemDto> items;

    public Long getCustomerId() { return customerId; }
    public String getStatus() { return status; }
    public Double getTotalAmount() { return totalAmount; }
    public String getBookingDate() { return bookingDate; }
    public String getNotes() { return notes; }
    public List<OrderItemDto> getItems() { return items; }

    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public void setStatus(String status) { this.status = status; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public void setBookingDate(String bookingDate) { this.bookingDate = bookingDate; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setItems(List<OrderItemDto> items) { this.items = items; }
}
