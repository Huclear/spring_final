package com.mpt.journal.data.service;

import com.mpt.journal.data.Paginator;
import com.mpt.journal.domain.entity.BanList;
import com.mpt.journal.domain.model.PagedResult;
import com.mpt.journal.domain.repository.BanListRepository;
import com.mpt.journal.domain.service.BanListService;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SomeBanListService implements BanListService {

    private BanListRepository _banList;

    public SomeBanListService(BanListRepository banList) {
        _banList = banList;
    }

    @Override
    public PagedResult<BanList> getBanListsList(int page, int pageSize, @Nullable Boolean isPermanent) {
        var ban_recs = _banList.findAll().stream().filter(it ->
                isPermanent == null || it.isPermanent() == isPermanent).toList();

        return Paginator.paginate(ban_recs, page, pageSize);
    }

    @Override
    public BanList getBanListByID(UUID ban_recID) {
        return _banList.findById(ban_recID).orElse(null);
    }

    @Override
    public BanList getBanListByUserID(UUID userID) {
        return _banList.findAll().stream().filter(it ->
                it.getUser() != null && it.getUser().getId() == userID).findFirst().orElse(null);
    }

    @Override
    public BanList addBanList(BanList ban_rec) {
        return _banList.save(ban_rec);
    }

    @Override
    public BanList editBanList(BanList ban_rec) {
        return _banList.save(ban_rec);
    }

    @Override
    public void deleteBanList(UUID ban_recID) {
        _banList.deleteById(ban_recID);
    }

    @Override
    public void deleteBanLists(List<UUID> ban_recIDs) {
        _banList.deleteAllById(ban_recIDs);
    }
}
