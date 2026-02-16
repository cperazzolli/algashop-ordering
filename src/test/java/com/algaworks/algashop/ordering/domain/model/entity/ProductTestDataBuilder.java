package com.algaworks.algashop.ordering.domain.model.entity;

import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Product;
import com.algaworks.algashop.ordering.domain.model.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ProductId;

public class ProductTestDataBuilder {

    private ProductTestDataBuilder() {
    }

    public static Product aProduct() {
        return Product.builder()
                .id(new ProductId())
                .name(new ProductName("Notebook x11"))
                .price(new Money("3000"))
                .inStock(true)
                .build();
    }

    public static Product.ProductBuilder aProductUnavailable() {
        return Product.builder()
                .id(new ProductId())
                .name(new ProductName("Notebook fx900"))
                .price(new Money("5000"))
                .inStock(false);
    }

    public static Product.ProductBuilder aProductAltRamMemory() {
        return Product.builder()
                .id(new ProductId())
                .name(new ProductName("Ram 4GB"))
                .price(new Money("200"))
                .inStock(true);
    }

    public static Product.ProductBuilder aProductAltMousePad() {
        return Product.builder()
                .id(new ProductId())
                .name(new ProductName("Mouse Pad"))
                .price(new Money("200"))
                .inStock(true);
    }
}
