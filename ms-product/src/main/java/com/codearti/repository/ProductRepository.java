package com.codearti.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.codearti.model.entity.CategoryEntity;
import com.codearti.model.entity.DeletedProduct;
import com.codearti.model.entity.ProductEntity;
import com.codearti.model.entity.ProductStatus;

public interface ProductRepository extends CrudRepository<ProductEntity, Long> {
	
	@Query("from ProductEntity where deleted = com.codearti.model.entity.DeletedProduct.CREATED and ((:status is null) or (status = :status))")
	List<ProductEntity> findAll(@Param("status") ProductStatus status);
	
	List<ProductEntity> findByCategoryAndDeleted(CategoryEntity category, DeletedProduct deleted);

}
