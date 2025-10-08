package com.mpt.journal.domain.service;


import com.mpt.journal.domain.entity.UserEntity;
import com.mpt.journal.domain.model.PagedResult;

import java.util.List;
import java.util.UUID;

public interface UsersService {
    PagedResult<UserEntity> getUsers(
            int page,
            int pageSize,
            String nickName,
            Integer minRecipesCount,
            Integer maxRecipesCount,
            Boolean showDeleted
    );

    UserEntity getUserByLogin(String login);

    UserEntity getUserById(UUID userID);

    UserEntity addUser(UserEntity user);

    UserEntity editUser(UserEntity user);

    void deleteUser(UUID userID);

    void deleteUsers(List<UUID> userIDs);

    void confirmDeleteUser(UUID userID);

    void confirmDeleteUsers(List<UUID> userIDs);
}
