package com.mpt.journal.data;

import com.mpt.journal.domain.model.PagedResult;

import java.util.ArrayList;
import java.util.List;

public class Paginator {
    public static <R> PagedResult<R> paginate(List<R> list, int page, int pageSize) {
        var totalSize = list.size();
        var totalPages = (int) Math.ceil((double) totalSize / pageSize);
        if (page > totalPages)
            return new PagedResult<>(new ArrayList<>(), totalPages, page, pageSize);
        return new PagedResult<>(
                list
                        .subList((page - 1) * pageSize, Math.min(totalSize, page * pageSize)),
                totalPages,
                page,
                pageSize);
    }
}
