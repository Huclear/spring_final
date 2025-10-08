package com.mpt.journal.domain.service;

import com.mpt.journal.domain.entity.FilterEntity;
import com.mpt.journal.domain.model.PagedResult;

import java.util.List;
import java.util.UUID;

public interface FiltersService {
    PagedResult<FilterEntity> getFiltersList(
            int page,
            int pageSize,
            String name,
            Boolean showDeleted
    );

    PagedResult<FilterEntity> getFiltersByRecipe(
            int page,
            int pageSize,
            UUID recipe_ID,
            String name,
            Boolean showDeleted
    );

    FilterEntity getFilterByID(UUID filterID);

    FilterEntity getFilterByName(String name);

    FilterEntity addFilter(FilterEntity filter);

    FilterEntity editFilter(FilterEntity filter);

    void deleteFilter(UUID filterID);

    void deleteFilters(List<UUID> filterIDs);

    void confirmDeleteFilter(UUID filterID);

    void confirmDeleteFilters(List<UUID> filterIDs);
}
