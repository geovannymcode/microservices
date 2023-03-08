package com.geovannycode.controller;

import com.geovannycode.model.dto.ProductCreateRequestDto;
import com.geovannycode.model.dto.ProductResponseDto;
import com.geovannycode.model.dto.ProductUpdateRequestDto;
import com.geovannycode.model.dto.ProductUpdateStockRequestDto;
import com.geovannycode.model.entity.ProductStatus;
import com.geovannycode.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path="v1")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    private final ServletWebServerApplicationContext webServerAppCtxt;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE})
    public ResponseEntity<List<ProductResponseDto>> findAll(@RequestParam(required = false) ProductStatus status){
        var port = webServerAppCtxt.getWebServer().getPort();
        var result = productService.findAll(status, port);
        if(result.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE})
    public ResponseEntity<ProductResponseDto> findById(@PathVariable Long id){
        var port = webServerAppCtxt.getWebServer().getPort();
        var result = productService.findById(id, port);
        return ResponseEntity.ok(result);
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ProductResponseDto> create(@Valid @RequestBody ProductCreateRequestDto productRequest){
        var port = webServerAppCtxt.getWebServer().getPort();
        var result = productService.create(productRequest, port);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @PutMapping(value= "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ProductResponseDto> update(@PathVariable Long id, @Valid @RequestBody ProductUpdateRequestDto productRequest){
        var port = webServerAppCtxt.getWebServer().getPort();
        var result = productService.update(id, productRequest, port);
        return ResponseEntity.ok(result);
    }

    @PatchMapping(value= "/{id}/stock", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ProductResponseDto> updateStock(@PathVariable Long id, @Valid @RequestBody ProductUpdateStockRequestDto productRequest){
        var port = webServerAppCtxt.getWebServer().getPort();
        var result = productService.updateStock(id, productRequest, port);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping(value= "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE})
    public ResponseEntity<ProductResponseDto>delete(@PathVariable Long id){
        var port = webServerAppCtxt.getWebServer().getPort();
        productService.delete(id, port);
        return ResponseEntity.noContent().build();
    }

}
