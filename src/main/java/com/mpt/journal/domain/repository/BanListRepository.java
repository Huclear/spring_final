package com.mpt.journal.domain.repository;

import com.mpt.journal.domain.entity.BanList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BanListRepository extends JpaRepository<BanList, UUID> {

}
