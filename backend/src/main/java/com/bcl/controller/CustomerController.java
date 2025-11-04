package com.bcl.controller;

import com.bcl.model.Customer;
import com.bcl.repo.CustomerRepository;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerRepository repo;
    public CustomerController(CustomerRepository repo){ this.repo = repo; }

    @GetMapping
    public List<Customer> all(){ return repo.findAll(); }

    @GetMapping("{id}")
    public ResponseEntity<Customer> getOne(@PathVariable Long id){
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Customer> create(@Valid @RequestBody Customer c){
        Customer saved = repo.save(c);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("{id}")
    public ResponseEntity<Customer> update(@PathVariable Long id, @Valid @RequestBody Customer c){
        return repo.findById(id).map(existing -> {
            c.setCustomerId(id);
            return ResponseEntity.ok(repo.save(c));
        }).orElse(ResponseEntity.notFound().build());
    }
}
