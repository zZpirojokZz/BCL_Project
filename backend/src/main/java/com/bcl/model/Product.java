package com.bcl.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotBlank @Size(max = 100)
    private String productName;

    @NotBlank
    private String category;

    @NotNull @DecimalMin("0.0")
    private Double price;

    @NotNull @DecimalMin("0.0")
    private Double cost;

    @Lob
    private String description;

    private Boolean seasonal = false;

    private Boolean active = true;

    private LocalDate introducedDate;

    @Lob
    private String ingredients;
}
