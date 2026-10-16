package com.mshenguDev.hfservice.services;

import com.mshenguDev.hfservice.entities.Order;
import com.mshenguDev.hfservice.entities.Dto.OrderBookingStatsDto;
import com.mshenguDev.hfservice.entities.Dto.OrderDto;

import java.util.List;

public interface OrderService {
    Order addOrder(OrderDto orderDto);
    List<Order> getOrdersByCustomer(Long customerId);
    List<Order> retrieveAllOrders();
    Order updateOrderStatus(Long orderId, String status);
    OrderBookingStatsDto getBookingStats();
}
