package com.geovannycode.service;

import com.geovannycode.configuration.error.ResourceNotFoundException;
import com.geovannycode.model.dto.ProductCreateRequestDto;
import com.geovannycode.model.dto.ProductResponseDto;
import com.geovannycode.model.dto.ProductUpdateRequestDto;
import com.geovannycode.model.dto.ProductUpdateStockRequestDto;
import com.geovannycode.model.entity.CategoryEntity;
import com.geovannycode.model.entity.DeletedProduct;
import com.geovannycode.model.entity.ProductEntity;
import com.geovannycode.model.entity.ProductStatus;
import com.geovannycode.model.mapper.ProductMapper;
import com.geovannycode.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper mapper;

    @Transactional(readOnly = true)
    public List<ProductResponseDto> findAll(ProductStatus status, int port) {
        log.info("findAll");
        var list = productRepository.findAllByStatus(status);
        log.info("finded");
        return list.stream().map(p -> mapper.entityToResponse(p, port)).toList();
    }

    @Transactional(readOnly = true)
    public ProductResponseDto findById(Long id, int port) {
        log.info("findById");
        return productRepository.findById(id).filter(p -> p.getDeleted() == DeletedProduct.CREATED).map(p -> mapper.entityToResponse(p, port)).orElseThrow(() -> new ResourceNotFoundException("Resource not found", HttpStatus.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDto> findByIdCategory(Long id, int port) {
        log.info("findByIdCategory");
        var list = productRepository.findByCategoryAndDeleted(CategoryEntity.builder().id(id).build(), DeletedProduct.CREATED);
        return list.stream().map(p -> mapper.entityToResponse(p, port)).collect(Collectors.toList());
    }

    @Transactional
    public ProductResponseDto create(ProductCreateRequestDto productRequest, int port) {
        log.info("create");
        var productEntity = mapper.requestToEntity(productRequest);
        productRepository.save(productEntity);
        log.info("saved");
        return mapper.entityToResponse(productEntity, port);
    }

    @Transactional
    public ProductResponseDto update(Long id, ProductUpdateRequestDto productRequest, int port){
        log.info("update");
        var productEntity = getProductById(id);
        BeanUtils.copyProperties(productRequest, productEntity);
        productEntity.setCategory(CategoryEntity.builder().id(productRequest.categoryId()).build());
        productRepository.save(productEntity);
        log.info("update");
        return mapper.entityToResponse(productEntity, port);
    }

    @Transactional
    public ProductResponseDto updateStock(Long id, ProductUpdateStockRequestDto productRequest, int port) {
        log.info("update");
        var productEntity = getProductById(id);
        productEntity.setStock(productEntity.getStock() + productRequest.stock());
        productRepository.save(productEntity);
        log.info("update");
        return mapper.entityToResponse(productEntity, port);
    }

    @Transactional
    public void delete(Long id, int port) {
        log.info("delete");
        var productEntity = getProductById(id);
        productEntity.setDeleted(DeletedProduct.DELETED);
        productRepository.save(productEntity);
        log.info("deleted");
    }

    private ProductEntity getProductById(Long id) {
        var productEntityOp = productRepository.findById(id)
                .filter(p->p.getDeleted()==DeletedProduct.CREATED);
        if(!productEntityOp.isPresent()){
            throw new ResourceNotFoundException("Resource not found", HttpStatus.NOT_FOUND);
        }
        return productEntityOp.get();
    }
}
