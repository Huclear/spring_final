package com.mpt.journal.domain.service;

import com.mpt.journal.domain.entity.BanList;
import com.mpt.journal.domain.model.PagedResult;
import jakarta.annotation.Nullable;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface BanListService {
    PagedResult<BanList> getBanListsList(
            int page,
            int pageSize,
            @Nullable Boolean isPermanent
    );

    BanList getBanListByID(UUID ban_recID);

    BanList getBanListByUserID(UUID userID);

    BanList addBanList(BanList ban_rec);

    BanList editBanList(BanList ban_rec);

    void deleteBanList(UUID ban_recID);

    void deleteBanLists(List<UUID> ban_recIDs);
}
