package com.mshenguDev.hfservice.services.impl;

import com.mshenguDev.hfservice.entities.Dto.OrderItemResponseDto;
import com.mshenguDev.hfservice.entities.Dto.OrderResponseDto;
import com.mshenguDev.hfservice.entities.Order;
import com.mshenguDev.hfservice.entities.OrderItem;
import com.mshenguDev.hfservice.repositories.OrderRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderServiceImplTest {
    @Test
    void orderResponseMapsItemsWithoutParentOrderReference() {
        Order order = new Order();
        order.setCustomerId(7L);
        order.setStatus("BOOKED");
        order.setTotalAmount(400.0);
        order.setBookingDate("2026-09-29");

        OrderItem item = new OrderItem();
        item.setComponentId(2L);
        item.setName("Component 2");
        item.setCategory("DINING_TABLE");
        item.setPrice(400.0);
        item.setQuantity(1);
        order.addItem(item);

        OrderResponseDto response = OrderResponseDto.from(order);
        OrderItemResponseDto responseItem = response.items().get(0);

        assertEquals(7L, response.customerId());
        assertEquals("BOOKED", response.status());
        assertEquals(1, response.items().size());
        assertEquals("Component 2", responseItem.name());
        assertEquals(2L, responseItem.componentId());
        assertEquals(1, responseItem.quantity());
    }

    @Test
    void updateOrderStatusPersistsApprovedStatus() {
        OrderRepository repository = mock(OrderRepository.class);
        OrderServiceImpl service = new OrderServiceImpl(repository);
        Order order = new Order();
        order.setStatus("BOOKED");
        when(repository.findById(3L)).thenReturn(Optional.of(order));
        when(repository.save(order)).thenReturn(order);

        Order result = service.updateOrderStatus(3L, "approved");

        assertEquals("APPROVED", result.getStatus());
        verify(repository).save(order);
    }
}
