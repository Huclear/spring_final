package com.mpt.journal.domain.repository;

import com.mpt.journal.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


public interface UsersRepository extends JpaRepository<UserEntity, UUID> {
}
