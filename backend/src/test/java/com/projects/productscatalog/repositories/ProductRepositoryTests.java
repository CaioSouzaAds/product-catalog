package com.projects.productscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.projects.productscatalog.entities.Product;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repository;

	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		// Arrange: Configuração do teste
		long existingId = 1L;

		// Act: Ação a ser testada
		// Exclui o objeto com o ID existente usando o método deleteById do repositório
		repository.deleteById(existingId);

		// Assert: Verificação do resultado
		// Tenta encontrar o objeto após a exclusão
		Optional<Product> result = repository.findById(existingId);

		// Verifica se o Optional está vazio, indicando que o objeto não foi encontrado
		Assertions.assertFalse(result.isPresent());
	}

}
