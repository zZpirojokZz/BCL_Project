package com.bcl.controller;

import com.bcl.model.Order;
import com.bcl.model.OrderItem;
import com.bcl.repo.OrderRepository;
import com.bcl.repo.ProductRepository;
import com.bcl.repo.CustomerRepository;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderRepository orderRepo;
    private final CustomerRepository customerRepo;
    private final ProductRepository productRepo;

    public OrderController(OrderRepository orderRepo, CustomerRepository customerRepo, ProductRepository productRepo){
        this.orderRepo = orderRepo;
        this.customerRepo = customerRepo;
        this.productRepo = productRepo;
    }

    @GetMapping
    public List<Order> all(){ return orderRepo.findAll(); }

    @GetMapping("{id}")
    public ResponseEntity<Order> one(@PathVariable Long id){
        return orderRepo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Order order){
        if(order.getCustomer() == null || order.getCustomer().getCustomerId() == null){
            return ResponseEntity.badRequest().body("Customer is required");
        }
        var cust = customerRepo.findById(order.getCustomer().getCustomerId());
        if(cust.isEmpty()) return ResponseEntity.badRequest().body("Customer not found");

        order.setCustomer(cust.get());
        order.setOrderDate(LocalDateTime.now());
        if(order.getItems() != null){
            for(OrderItem it : order.getItems()){
                it.setOrder(order);
                var prodOpt = productRepo.findById(it.getProduct().getProductId());
                prodOpt.ifPresent(it::setProduct);
            }
        }
        Order saved = orderRepo.save(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("{id}/complete")
    public ResponseEntity<Order> complete(@PathVariable Long id){
        return orderRepo.findById(id).map(o -> {
            o.setStatus("Completed");
            orderRepo.save(o);
            return ResponseEntity.ok(o);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("{id}/cancel")
    public ResponseEntity<Order> cancel(@PathVariable Long id){
        return orderRepo.findById(id).map(o -> {
            o.setStatus("Cancelled");
            orderRepo.save(o);
            return ResponseEntity.ok(o);
        }).orElse(ResponseEntity.notFound().build());
    }
}
