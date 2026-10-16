package com.mshenguDev.hfservice.controllers;

import com.mshenguDev.hfservice.entities.Dto.OrderBookingStatsDto;
import com.mshenguDev.hfservice.entities.Dto.OrderDto;
import com.mshenguDev.hfservice.entities.Dto.OrderResponseDto;
import com.mshenguDev.hfservice.entities.Dto.OrderStatusUpdateDto;
import com.mshenguDev.hfservice.entities.Order;
import com.mshenguDev.hfservice.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(originPatterns = "*", allowCredentials = "true")
@RestController
@RequestMapping(path = "/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping(path = "/addOrder")
    public ResponseEntity<OrderResponseDto> addOrder(@RequestBody OrderDto orderDto){
        Order saved = orderService.addOrder(orderDto);
        return ResponseEntity.ok(OrderResponseDto.from(saved));
    }

    @GetMapping(path = "/getByCustomerId/{customerId}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByCustomer(@PathVariable Long customerId){
        List<Order> orders = orderService.getOrdersByCustomer(customerId);
        return ResponseEntity.ok(orders.stream().map(OrderResponseDto::from).toList());
    }

    @GetMapping(path = "/getAllOrders")
    public ResponseEntity<List<OrderResponseDto>> getAllOrders(){
        List<Order> orders = orderService.retrieveAllOrders();
        return ResponseEntity.ok(orders.stream().map(OrderResponseDto::from).toList());
    }

    @PutMapping(path = "/{orderId}")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody OrderStatusUpdateDto update) {
        Order updated = orderService.updateOrderStatus(orderId, update.status());
        return ResponseEntity.ok(OrderResponseDto.from(updated));
    }

    @GetMapping(path = "/booking-stats")
    public ResponseEntity<OrderBookingStatsDto> getBookingStats() {
        return ResponseEntity.ok(orderService.getBookingStats());
    }
}
