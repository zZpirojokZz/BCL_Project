package com.bcl.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Customer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Min(0)
    private Integer age;

    @Column(length = 1)
    private String gender;

    private String postalCode;

    @Email
    @Column(unique = true)
    private String email;

    private String phoneNumber;

    private String membershipStatus = "Basic";

    private LocalDate joinDate;

    private LocalDate lastPurchaseDate;

    private Double totalSpending = 0.0;

    private Double averageOrderValue;

    private String frequency;

    private String preferredCategory;

    private Boolean churned = false;
}
