package com.projects.productscatalog.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import com.projects.productscatalog.dto.ProductDTO;
import com.projects.productscatalog.repositories.ProductRepository;
import com.projects.productscatalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest
public class ProductServiceIT {
    
    @Autowired
    private ProductService service;
    
    @Autowired
    private ProductRepository repository;
    
    private Long existingId;
    private Long nonExistingId;
    
    
    @BeforeEach
    void setup() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        
    }
    
    @Test
    public void findAllShouldReturnPage() {
      
    	PageRequest pageRequest = PageRequest.of(0, 10);
    	
        Page<ProductDTO> page = service.findAllPaged(pageRequest);

        Assertions.assertFalse(page.isEmpty());
        Assertions.assertEquals(0, page.getNumber());
        Assertions.assertEquals(10, page.getSize());
    
    }
    
    @Test
    public void deleteShouldDeleteResourceWhenIdExists() {
        // Obtém a contagem total de produtos antes da exclusão
        long countBeforeDeletion = repository.count();
        
        // Realiza a exclusão do recurso existente
        service.delete(existingId);
        
        // Obtém a contagem total de produtos após a exclusão
        long countAfterDeletion = repository.count();
        
        // Verifica se a contagem foi reduzida em 1 após a exclusão
        assertEquals(countBeforeDeletion - 1, countAfterDeletion);
        
        
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        // Utiliza uma assertiva para verificar se a exceção ResourceNotFoundException é lançada
        assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });
    }
}

