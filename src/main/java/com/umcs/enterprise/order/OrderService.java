package com.umcs.enterprise.order;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    @NonNull
    private  OrderRepository  orderRepository;
    public Order pay(Order order) {

        order.setStatus(OrderStatus.PAID);
    return  orderRepository.save(order);
    }

    public Order ship(Order order) {


        order.setStatus(OrderStatus.SHIPPED);
        return  orderRepository.save(order);
    }
}
