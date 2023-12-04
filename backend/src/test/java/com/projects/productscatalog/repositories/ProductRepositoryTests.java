package com.projects.productscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.projects.productscatalog.entities.Product;
import com.projects.productscatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repository;

	private long existingId;
	private long nonExistentId;
	private long countTotalProducts;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistentId = 1000L;
		countTotalProducts = 25L;
	}

	@Test
	public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
		// Arrange
		Product product = Factory.createdProduct();
		product.setId(null);

		// Act
		product = repository.save(product);

		// Assert
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts + 1, product.getId());
	}

	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		// Arrange

		// Act
		repository.deleteById(existingId);

		// Assert
		Optional<Product> result = repository.findById(existingId);
		Assertions.assertFalse(result.isPresent());
	}

	@Test
	public void shouldReturnFalseWhenCheckingExistenceOfNonExistentId() {
		// Arrange
		boolean expectedIdExists = false;

		// Act
		boolean actualIdExists = repository.existsById(nonExistentId);

		// Assert
		Assertions.assertEquals(expectedIdExists, actualIdExists);
	}

	@Test
	public void findByIdShouldReturnNonEmptyOptionalWhenIdExists() {
		// Arrange

		// Act
		Optional<Product> result = repository.findById(existingId);

		// Assert
		Assertions.assertTrue(result.isPresent());
	}

	@Test
	public void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExist() {
		// Arrange

		// Act
		Optional<Product> result = repository.findById(nonExistentId);

		// Assert
		Assertions.assertFalse(result.isPresent());
	}
}
