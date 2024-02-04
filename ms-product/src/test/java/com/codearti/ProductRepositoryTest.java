package com.codearti;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.codearti.model.entity.CategoryEntity;
import com.codearti.model.entity.DeletedProduct;
import com.codearti.model.entity.ProductEntity;
import com.codearti.repository.ProductRepository;

@DataJpaTest
public class ProductRepositoryTest {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Test
	void whenGetAll_ThenReturnAllProduct() {
		var list = productRepository.findAll(null);
		assertEquals(3, list.size());
	}
	
	@Test
	void whenValidGetId_ThenReturnProduct() {
		Optional<ProductEntity> productEntity = productRepository.findById(1L);
		assertTrue(productEntity.isPresent());
		assertEquals("Laptop", productEntity.orElseThrow().getName());
	}
	
	@Test
	void whenInValidGetId_ThenNotFound() {
		Optional<ProductEntity> productEntity = productRepository.findById(55L);
		assertThrows(NoSuchElementException.class, productEntity::orElseThrow);
		assertTrue(!productEntity.isPresent());
	}
	
	@Test
	void whenValidSave_ThenReturnPRoduct() {
		var productEntity = ProductEntity.builder()
				.name("Teclado")
				.stock(Double.valueOf(10))
				.price(BigDecimal.valueOf(300))
				.category(CategoryEntity.builder().id(1L).build())
				.build();
		productRepository.save(productEntity);
		
		var product = productRepository.findByCategoryAndDeleted(productEntity.getCategory(), DeletedProduct.CREATED);
		assertEquals(4, product.size());
	}

}
