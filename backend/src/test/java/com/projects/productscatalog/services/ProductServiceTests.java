package com.projects.productscatalog.services;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.projects.productscatalog.dto.ProductDTO;
import com.projects.productscatalog.entities.Product;
import com.projects.productscatalog.repositories.ProductRepository;
import com.projects.productscatalog.services.exceptions.DataBaseException;
import com.projects.productscatalog.services.exceptions.ResourceNotFoundException;
import com.projects.productscatalog.tests.Factory;

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
	private PageImpl<Product> page;
	private Product product;

	// Método executado antes de cada teste para configurar o ambiente
	@BeforeEach
	void setUp() throws Exception {
		// Define IDs para teste
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		product = Factory.createdProduct();
		page = new PageImpl<>(List.of(product));

		Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);

		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));

		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

		// Configuração do mock para retornar true para existsById com existingId
		Mockito.when(repository.existsById(existingId)).thenReturn(true);

		// Configuração do mock para lançar ResourceNotFoundException para existsById
		// com nonExistingId
		Mockito.when(repository.existsById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

		// Configura o mock para lançar DataIntegrityViolationException para deleteById
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

	}

	@Test
	public void findAllPagedShouldReturnPage() {

		Pageable pageable = PageRequest.of(0, 10);

		Page<ProductDTO> result = service.findAllPaged(pageable);

		Assertions.assertNotNull(result);
		Mockito.verify(repository, Mockito.times(1)).findAll(pageable);

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
