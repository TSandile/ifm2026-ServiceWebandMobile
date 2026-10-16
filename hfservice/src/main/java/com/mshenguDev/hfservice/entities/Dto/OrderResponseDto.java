package com.mshenguDev.hfservice.entities.Dto;

import com.mshenguDev.hfservice.entities.Order;

import java.util.List;

public record OrderResponseDto(
        Long id,
        Long customerId,
        String status,
        Double totalAmount,
        String bookingDate,
        String notes,
        List<OrderItemResponseDto> items) {

    public static OrderResponseDto from(Order order) {
        return new OrderResponseDto(
                order.getId(),
                order.getCustomerId(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getBookingDate(),
                order.getNotes(),
                order.getItems().stream().map(OrderItemResponseDto::from).toList());
    }
}
