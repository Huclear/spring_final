package com.mpt.journal.domain.repository;


import com.mpt.journal.domain.entity.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


public interface RecipesRepository extends JpaRepository<RecipeEntity, UUID> {
}
