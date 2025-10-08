package com.mpt.journal.domain.service;

import com.mpt.journal.domain.entity.ReviewEntity;
import com.mpt.journal.domain.model.Measure;
import com.mpt.journal.domain.model.PagedResult;
import com.mpt.journal.domain.model.ReviewsGroups;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.UUID;

public interface ReviewsService {
    PagedResult<ReviewEntity> getReviewsList(
            int page,
            int pageSize,
            @Nullable UUID recipe_id,
            @Nullable UUID user_id,
            @Nullable ReviewsGroups group
    );

    ReviewEntity getReviewByID(UUID reviewID);

    ReviewEntity addReview(ReviewEntity review);

    ReviewEntity editReview(ReviewEntity review);

    void deleteReview(UUID reviewID);

    void deleteReviews(List<UUID> reviewIDs);

    void confirmDeleteReview(UUID reviewID);

    void confirmDeleteReviews(List<UUID> reviewIDs);
}
