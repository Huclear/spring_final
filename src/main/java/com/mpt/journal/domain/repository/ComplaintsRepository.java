package com.mpt.journal.domain.repository;

import com.mpt.journal.domain.entity.ComplaintEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ComplaintsRepository extends JpaRepository<ComplaintEntity, UUID> {
}
