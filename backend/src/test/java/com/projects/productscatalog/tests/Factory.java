package com.projects.productscatalog.tests;

import java.time.Instant;

import com.projects.productscatalog.dto.ProductDTO;
import com.projects.productscatalog.entities.Category;
import com.projects.productscatalog.entities.Product;

public class Factory {
	
	public static Product createdProduct() {
		Product product = new Product(1L, "Phone", "Good Phone", 800.0, "https://img.com/img.png", Instant.parse("2020-10-20T03:00:00Z"));
		product.getCategories().add(createdCategory());
		return product;
	}
	
	public static ProductDTO createProductDTO() {
		Product product = createdProduct();
		return new ProductDTO(product, product.getCategories());
	}
	
	public static Category createdCategory() {
		return new Category(2L, "Electronics");
	}
}
