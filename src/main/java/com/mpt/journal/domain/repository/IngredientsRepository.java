package com.mpt.journal.domain.repository;

import com.mpt.journal.domain.entity.IngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


public interface IngredientsRepository extends JpaRepository<IngredientEntity, UUID> {
}
