package com.frcalderon.products.controller;

import com.frcalderon.products.controller.dto.ManageProductsRequest;
import com.frcalderon.products.controller.dto.ProductRequest;
import com.frcalderon.products.controller.dto.ProductResponse;
import com.frcalderon.products.model.Product;
import com.frcalderon.products.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {
        List<Product> products = this.productService.getAllProducts();
        return products.stream().map(ProductResponse::new).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse getProduct(@PathVariable Long id) {
        Product product = this.productService.getProduct(id);
        return new ProductResponse(product);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@RequestBody ProductRequest productRequest) {
        Product product = this.productService.createProduct(productRequest);
        return new ProductResponse(product);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        Product product = this.productService.updateProduct(id, productRequest);
        return new ProductResponse(product);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        this.productService.deleteProduct(id);
    }

    @PostMapping("/stock/add")
    @ResponseStatus(HttpStatus.OK)
    public void addStockToProduct(@RequestBody List<ManageProductsRequest> manageProductsRequestList) {
        this.productService.addProducts(manageProductsRequestList);
    }

    @PostMapping("/stock/consume")
    @ResponseStatus(HttpStatus.OK)
    public void consumeStockFromIngredient(@RequestBody List<ManageProductsRequest> manageProductsRequestList) {
        this.productService.consumeProducts(manageProductsRequestList);
    }
}
