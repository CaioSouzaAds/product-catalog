package com.projects.productscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.projects.productscatalog.repositories.ProductRepository;
import com.projects.productscatalog.services.exceptions.DataBaseException;
import com.projects.productscatalog.services.exceptions.ResourceNotFoundException;

//Importa as anotações e classes necessárias para os testes com Spring e Mockito
@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	// Cria uma instância de ProductService e injeta o mock repository
	@InjectMocks
	private ProductService service;

	// Cria um mock para ProductRepository
	@Mock
	private ProductRepository repository;

	// Variáveis para armazenar IDs existentes e não existentes
	private long existingId;
	private long nonExistingId;
	private long dependentId;

	// Método executado antes de cada teste para configurar o ambiente
	@BeforeEach
	void setUp() throws Exception {
		// Define IDs para teste
		existingId = 1L;
		nonExistingId = 1000L;
		dependentId = 4L;

		// Configuração do mock para retornar true para existsById com existingId
		Mockito.when(repository.existsById(existingId)).thenReturn(true);

		// Configuração do mock para lançar ResourceNotFoundException para existsById
		// com nonExistingId
		Mockito.when(repository.existsById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

		// Configura o mock para lançar DataIntegrityViolationException para deleteById
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

	}

	@Test
	public void deleteShouldThrowDataBaseExceptionWhenDataIntegrityViolationExceptionIsThrown() {
		// Configura o mock para retornar true para existsById com dependentId
		Mockito.when(repository.existsById(dependentId)).thenReturn(true);

		// Executa o método delete e verifica se lança a exceção esperada
		Assertions.assertThrows(DataBaseException.class, () -> {
			service.delete(dependentId);
		});

		// Verifica se o método deleteById do mock repository foi chamado exatamente uma
		// vez com o ID existente
		Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId);

	}

	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {

		// Executa o método delete e verifica se não lança exceções
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});

		// Verifica se o método deleteById do mock repository foi chamado exatamente uma
		// vez com o ID existente
		Mockito.verify(repository, Mockito.times(1)).existsById(nonExistingId);
	}

	// Teste para verificar se o método delete funciona corretamente quando chamado
	// com um ID existente
	@Test
	public void deleteShouldDoNothingWhenIdExists() {

		// Executa o método delete e verifica se não lança exceções
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});

		// Verifica se o método deleteById do mock repository foi chamado exatamente uma
		// vez com o ID existente
		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
	}
}
