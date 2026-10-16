package com.mshenguDev.hfservice.services.impl;

import com.mshenguDev.hfservice.entities.Order;
import com.mshenguDev.hfservice.entities.OrderItem;
import com.mshenguDev.hfservice.entities.Dto.OrderBookingStatsDto;
import com.mshenguDev.hfservice.entities.Dto.OrderDto;
import com.mshenguDev.hfservice.entities.Dto.OrderItemDto;
import com.mshenguDev.hfservice.repositories.OrderRepository;
import com.mshenguDev.hfservice.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order addOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setCustomerId(orderDto.getCustomerId());
        order.setStatus(orderDto.getStatus());
        order.setTotalAmount(orderDto.getTotalAmount());
        order.setBookingDate(orderDto.getBookingDate());
        order.setNotes(orderDto.getNotes());

        if(orderDto.getItems() != null){
            for(OrderItemDto it : orderDto.getItems()){
                OrderItem item = new OrderItem();
                item.setComponentId(it.getComponentId());
                item.setName(it.getName());
                item.setCategory(it.getCategory());
                item.setPrice(it.getPrice());
                item.setQuantity(it.getQuantity());
                order.addItem(item);
            }
        }

        return orderRepository.save(order);
    }

    @Override
    public List<Order> getOrdersByCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Order> retrieveAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order updateOrderStatus(Long orderId, String status) {
        if (status == null || status.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order status is required.");
        }

        String normalizedStatus = status.trim().toUpperCase(Locale.ROOT);
        if (!normalizedStatus.equals("APPROVED") && !normalizedStatus.equals("DECLINED")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Order status must be APPROVED or DECLINED.");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Order " + orderId + " was not found."));

        order.setStatus(normalizedStatus);
        return orderRepository.save(order);
    }

    @Override
    public OrderBookingStatsDto getBookingStats() {
        List<Order> orders = orderRepository.findAll();
        long totalOrders = orders.size();
        long approvedOrders = orders.stream().filter(order -> "APPROVED".equalsIgnoreCase(order.getStatus())).count();
        long declinedOrders = orders.stream().filter(order -> "DECLINED".equalsIgnoreCase(order.getStatus())).count();
        long pendingOrders = orders.stream().filter(order -> order.getStatus() == null || order.getStatus().isBlank() || (!"APPROVED".equalsIgnoreCase(order.getStatus()) && !"DECLINED".equalsIgnoreCase(order.getStatus()))).count();
        double totalRevenue = orders.stream()
                .filter(order -> order.getTotalAmount() != null)
                .mapToDouble(Order::getTotalAmount)
                .sum();

        return new OrderBookingStatsDto(totalOrders, approvedOrders, declinedOrders, pendingOrders, totalRevenue);
    }
}
