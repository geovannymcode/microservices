package com.geovannycode;

import com.geovannycode.model.entity.CategoryEntity;
import com.geovannycode.model.entity.DeletedProduct;
import com.geovannycode.model.entity.ProductEntity;
import com.geovannycode.model.entity.ProductStatus;
import com.geovannycode.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void whenGetAll_ThenReturnAllProduct(){
        var list = productRepository.findAllByStatus(ProductStatus.OLD);
        assertEquals(0,list.size());
    }

    @Test
    void whenValidSave_ThenReturnProduct(){
        var productCategory = ProductEntity.builder()
                .name("Teclado")
                .stock(Double.valueOf(10))
                .price(BigDecimal.valueOf(300))
                .category(CategoryEntity.builder().id(1L).build())
                .build();

        productRepository.save(productCategory);

        var list = productRepository.findByCategoryAndDeleted(productCategory.getCategory(), DeletedProduct.CREATED);
        assertEquals(4, list.size());
    }
}
