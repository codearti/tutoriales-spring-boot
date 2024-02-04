package com.codearti;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.codearti.model.dto.ProductResponseDto;
import com.codearti.model.dto.ProductUpdateStockRequestDto;
import com.codearti.model.entity.CategoryEntity;
import com.codearti.model.entity.DeletedProduct;
import com.codearti.model.entity.ProductEntity;
import com.codearti.model.mapper.ProductMapper;
import com.codearti.repository.ProductRepository;
import com.codearti.service.ProductService;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
	
	@Mock
	private ProductRepository productRepository;
	
	private ProductMapper mapper = Mappers.getMapper(ProductMapper.class);
	
	private ProductService productService;
	
	@BeforeEach
	void setUp() {
		productService = new ProductService(productRepository, mapper);
		
		var productEntity = ProductEntity.builder()
				.id(1L)
				.name("Teclado2")
				.stock(Double.valueOf(11))
				.price(BigDecimal.valueOf(300))
				.category(CategoryEntity.builder().id(1L).build())
				.deleted(DeletedProduct.CREATED)
				.build();
		
		Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(productEntity));
	}
	
	@Test
	public void whenValidGetId_ThenReturnProduct() {
		var productoResponse = productService.findById(1L, 10);
		assertEquals("Teclado2", productoResponse.getName());
	}
	
	@Test
	public void whenValidUpdateStock_ThenReturnNewStock() {
		var request = new ProductUpdateStockRequestDto(5D);
		ProductResponseDto productoResponse = productService.updateStock(1L, request, 10);
		assertEquals(16D, productoResponse.getStock());
	}

}
