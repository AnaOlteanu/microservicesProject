package com.example.order.service;

import com.example.order.model.Order;

import java.util.List;

public interface OrderService {
    Order save(Order order);
    Order delete(Long id);
    List<Order> findAll();

    Order findById(Long id);
}
