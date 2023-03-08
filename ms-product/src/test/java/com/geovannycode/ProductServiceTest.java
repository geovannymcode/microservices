package com.geovannycode;

import com.geovannycode.model.entity.CategoryEntity;
import com.geovannycode.model.entity.DeletedProduct;
import com.geovannycode.model.entity.ProductEntity;
import com.geovannycode.repository.ProductRepository;
import com.geovannycode.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ProductServiceTest {

    @MockBean
    private ServletWebServerApplicationContext webServerAppCtxt;
    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

   // @Autowired
   // private ProductMapper mapper;

    @BeforeEach
    public void setup(){
      //  MockitoAnnotations.openMocks(productRepository);
      //  productService = new ProductService(productRepository, mapper);

        ProductEntity productEntity = ProductEntity.builder()
                .id(1L)
                .name("Teclado2")
                .stock(Double.valueOf(10))
                .price(BigDecimal.valueOf(300))
                .category(CategoryEntity.builder().id(1L).build())
                .deleted(DeletedProduct.CREATED)
                .build();

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(productEntity));
    }

    @Test
    public void whenValidGetId_ThenReturnProduct(){
        var product = productService.findById(1L, 10);
        assertEquals("Teclado2", product.getName());
    }
}
