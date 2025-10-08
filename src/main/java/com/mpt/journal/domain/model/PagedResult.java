package com.mpt.journal.domain.model;

import java.util.List;
import java.util.function.Function;

public class PagedResult<T> {
    private final List<T> value;
    private final long totalPages;
    private final int currentPage;
    private final int pageSize;

    public PagedResult(List<T> value, long totalPages, int currentPage, int pageSize) {
        this.value = value;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.pageSize = pageSize;
    }

    public <R> PagedResult<R> map(Function<? super T, R> mapper) {
        var newValue = this.value.stream().map(mapper).toList();
        return new PagedResult(newValue, this.totalPages, this.currentPage, this.pageSize);
    }

    public List<T> getValue() {
        return value;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }
}
