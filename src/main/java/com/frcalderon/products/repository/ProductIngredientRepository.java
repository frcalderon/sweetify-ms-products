package com.frcalderon.products.repository;

import com.frcalderon.products.model.ProductIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductIngredientRepository extends JpaRepository<ProductIngredient, Long> {

    List<ProductIngredient> findAllByIngredientId(Long ingredientId);

    List<ProductIngredient> findAllByProductId(Long productId);

    void deleteByProductId(Long productId);
}
