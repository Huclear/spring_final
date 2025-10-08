package com.mpt.journal.data.service;

import com.mpt.journal.data.Paginator;
import com.mpt.journal.domain.entity.FilterEntity;
import com.mpt.journal.domain.model.PagedResult;
import com.mpt.journal.domain.repository.FilterRepository;
import com.mpt.journal.domain.service.FiltersService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SomeFiltersService implements FiltersService {

    private FilterRepository _filters;

    public SomeFiltersService(FilterRepository filters) {
        this._filters = filters;
    }

    @Override
    public PagedResult<FilterEntity> getFiltersList(int page, int pageSize, String name, Boolean showDeleted) {
        var response = _filters.findAll().stream()
                .filter(f ->
                        f.getDeleted() == (showDeleted != null && showDeleted) &&
                                (name == null || f.getName().equals(name))
                ).toList();
        return Paginator.paginate(response, page, pageSize);
    }

    @Override
    public PagedResult<FilterEntity> getFiltersByRecipe(int page, int pageSize, UUID recipe_ID, String name, Boolean showDeleted) {
        var response = _filters.findAll().stream()
                .filter(f ->
                        f.getDeleted() == (showDeleted != null && showDeleted) &&
                                (recipe_ID == null || f.getRecipes().stream().anyMatch(r -> r.getId() == recipe_ID)) &&
                                (name == null || f.getName().equals(name))
                ).toList();
        return Paginator.paginate(response, page, pageSize);
    }

    @Override
    public FilterEntity getFilterByID(UUID filterID) {
        return _filters.findById(filterID).orElse(null);
    }

    @Override
    public FilterEntity getFilterByName(String name) {
        return _filters.findAll().stream().filter(f ->
                f.getName().equals(name)
        ).findFirst().orElse(null);
    }

    @Override
    public FilterEntity addFilter(FilterEntity filter) {
        return _filters.save(filter);
    }

    @Override
    public FilterEntity editFilter(FilterEntity filter) {
        return _filters.save(filter);
    }

    @Override
    public void deleteFilter(UUID filterID) {
        var filter = getFilterByID(filterID);
        if (filter == null)
            return;
        if (filter.getDeleted())
            confirmDeleteFilter(filterID);
        else {
            filter.setDeleted(true);
            _filters.save(filter);
        }
    }

    @Override
    public void deleteFilters(List<UUID> filterIDs) {
        filterIDs.forEach(this::confirmDeleteFilter);
    }

    @Override
    public void confirmDeleteFilter(UUID filterID) {
        _filters.deleteById(filterID);
    }

    @Override
    public void confirmDeleteFilters(List<UUID> filterIDs) {
        _filters.deleteAllById(filterIDs);
    }
}
