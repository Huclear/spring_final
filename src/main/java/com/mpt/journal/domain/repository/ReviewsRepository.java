package com.mpt.journal.domain.repository;

import com.mpt.journal.domain.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReviewsRepository extends JpaRepository<ReviewEntity, UUID> {
}
