package com.bcl.controller;

import com.bcl.model.Product;
import com.bcl.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService svc;
    public ProductController(ProductService svc){ this.svc = svc; }

    @GetMapping
    public List<Product> getAll(){ return svc.findAll(); }

    @GetMapping("{id}")
    public ResponseEntity<Product> getOne(@PathVariable Long id){
        Product p = svc.findById(id);
        if(p==null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(p);
    }

    @PostMapping
    public ResponseEntity<Product> create(@Valid @RequestBody Product product){
        if(product.getCost() != null && product.getPrice() != null && product.getCost() > product.getPrice()){
            return ResponseEntity.badRequest().build();
        }
        Product saved = svc.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @Valid @RequestBody Product product){
        Product existing = svc.findById(id);
        if(existing == null) return ResponseEntity.notFound().build();
        product.setProductId(id);
        Product saved = svc.save(product);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        Product existing = svc.findById(id);
        if(existing == null) return ResponseEntity.notFound().build();
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }
}
