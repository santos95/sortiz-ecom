package com.ecommerce.sortizecom.model;

import jakarta.persistence.*;
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
    private String productName;
    private String description;
    private String image;
    private Integer quantity;
    private Double price;
    private Double specialPrice; // price after discount
    private Double discount;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

}
