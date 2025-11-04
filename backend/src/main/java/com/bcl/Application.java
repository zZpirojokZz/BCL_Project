package com.bcl;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.bcl.model.*;
import com.bcl.repo.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class Application {
    public static void main(String[] args){ SpringApplication.run(Application.class, args); }

    // sample data initializer
    @Bean
    CommandLineRunner init(ProductRepository productRepo, CustomerRepository customerRepo, OrderRepository orderRepo){
        return args -> {
            if(productRepo.count() == 0){
                Product p1 = Product.builder().productName("Croissant").category("Pastry").price(1.5).cost(0.6)
                        .active(true).seasonal(false).introducedDate(LocalDate.now()).description("Buttery croissant").build();
                Product p2 = Product.builder().productName("Pain au chocolat").category("Pastry").price(1.8).cost(0.7)
                        .active(true).seasonal(false).introducedDate(LocalDate.now()).description("Chocolate pastry").build();
                productRepo.saveAll(List.of(p1,p2));
            }
            if(customerRepo.count() == 0){
                Customer c = Customer.builder().firstName("Ivan").lastName("Petrov").email("ivan@example.com")
                        .joinDate(LocalDate.now().minusYears(1)).membershipStatus("Basic").totalSpending(0.0).build();
                customerRepo.save(c);
            }
        };
    }
}
