package com.ecommerce.sortizecom.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min = 5, message = "Product name must contain at least 4 characters")
    private String productName;
    @Size(min = 5, message = "Product name must contain at least 4 characters")
    private String description;
    private String image;

    private Integer quantity;

    @Min(0)
    private Double price;
    @Min(0)
    private Double specialPrice; // price after discount
    @Min(0)
    private Double discount;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

}
