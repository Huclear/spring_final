package com.mpt.journal.data.service;

import com.mpt.journal.data.Paginator;
import com.mpt.journal.domain.entity.UserEntity;
import com.mpt.journal.domain.model.PagedResult;
import com.mpt.journal.domain.repository.RecipesRepository;
import com.mpt.journal.domain.repository.UsersRepository;
import com.mpt.journal.domain.service.UsersService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SomeUsersService implements UsersService {
    private final UsersRepository _users;

    public SomeUsersService(UsersRepository users) {
        this._users = users;
    }

    @Override
    public PagedResult<UserEntity> getUsers(
            int page,
            int pageSize,
            String nickName,
            Integer minRecipesCount,
            Integer maxRecipesCount,
            Boolean showDeleted
    ) {
        var users = _users.findAll()
                .stream()
                .filter(u -> {
                            var recipesCount = u.getRecipes().size();
                            return
                                    (minRecipesCount == null || recipesCount >= minRecipesCount)
                                            && (maxRecipesCount == null || recipesCount <= maxRecipesCount)
                                            && (nickName == null || u.getNickname().contains(nickName))
                                            && u.getDeleted() == (showDeleted == null ? false : showDeleted);
                        }
                ).toList();

        return Paginator.paginate(users, page, pageSize);
    }

    @Override
    public UserEntity getUserByLogin(String login) {
        return _users.findAll().stream()
                .filter(u -> u.getLogin().equals(login))
                .findFirst().orElse(null);
    }

    @Override
    public UserEntity getUserById(UUID userID) {
        return _users.findById(userID).orElse(null);
    }

    @Override
    public UserEntity addUser(UserEntity user) {
        return _users.save(user);
    }

    @Override
    public UserEntity editUser(UserEntity user) {
        return _users.save(user);
    }

    @Override
    public void deleteUser(UUID userID) {
        var user = _users.findById(userID).orElse(null);
        if (user == null)
            return;
        if (user.getDeleted())
            confirmDeleteUser(userID);
        else {
            user.setDeleted(true);
            _users.save(user);
        }
    }

    @Override
    public void deleteUsers(List<UUID> userIDs) {
        userIDs.forEach(this::deleteUser);
    }

    @Override
    public void confirmDeleteUser(UUID userID) {
        _users.deleteById(userID);
    }

    @Override
    public void confirmDeleteUsers(List<UUID> userIDs) {
        userIDs.forEach(this::confirmDeleteUser);
    }
}
