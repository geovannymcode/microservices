package com.geovannycode.repository;

import com.geovannycode.model.entity.CategoryEntity;
import com.geovannycode.model.entity.DeletedProduct;
import com.geovannycode.model.entity.ProductEntity;
import com.geovannycode.model.entity.ProductStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends CrudRepository<ProductEntity, Long> {


    @Query("from ProductEntity where deleted = com.geovannycode.model.entity.DeletedProduct.CREATED and ((:status is null) or (status = :status))")
    List<ProductEntity> findAllByStatus(@Param("status") ProductStatus status);

    List<ProductEntity> findByCategoryAndDeleted(CategoryEntity category, DeletedProduct deleted);
}
