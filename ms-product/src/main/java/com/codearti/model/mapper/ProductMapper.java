package com.codearti.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.codearti.model.dto.ProductCreateRequestDto;
import com.codearti.model.dto.ProductResponseDto;
import com.codearti.model.entity.ProductEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

	@Mapping(source = "entity.id", target = "id")
	@Mapping(source = "entity.name", target = "name")
	@Mapping(source = "entity.stock", target = "stock")
	@Mapping(source = "entity.price", target = "price")
	@Mapping(source = "entity.creationDate", target = "creationDate")
	@Mapping(source = "entity.updateDate", target = "updateDate")
	@Mapping(source = "entity.status", target = "status")
	@Mapping(source = "entity.category.id", target = "categoryId")
	@Mapping(source = "port", target = "port")
    ProductResponseDto entityToResponse(ProductEntity entity, Integer port);
	
	@Mapping(source = "request.name", target = "name")
	@Mapping(source = "request.stock", target = "stock")
	@Mapping(source = "request.price", target = "price")
	@Mapping(source = "request.categoryId", target = "category.id")
	ProductEntity requestToEntity(ProductCreateRequestDto request);

}