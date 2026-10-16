package com.mshenguDev.hfservice.entities.Dto;

import com.mshenguDev.hfservice.entities.OrderItem;

public record OrderItemResponseDto(
        Long id,
        Long componentId,
        String name,
        String category,
        Double price,
        Integer quantity) {

    public static OrderItemResponseDto from(OrderItem item) {
        return new OrderItemResponseDto(
                item.getId(),
                item.getComponentId(),
                item.getName(),
                item.getCategory(),
                item.getPrice(),
                item.getQuantity());
    }
}
