package com.mpt.journal.data.service;

import com.mpt.journal.data.Paginator;
import com.mpt.journal.domain.entity.ReviewEntity;
import com.mpt.journal.domain.model.PagedResult;
import com.mpt.journal.domain.model.ReviewsGroups;
import com.mpt.journal.domain.repository.ReviewsRepository;
import com.mpt.journal.domain.service.ReviewsService;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SomeReviewsService implements ReviewsService {
    private final ReviewsRepository _reviews;

    public SomeReviewsService(ReviewsRepository reviews) {
        this._reviews = reviews;
    }

    @Override
    public PagedResult<ReviewEntity> getReviewsList(int page, int pageSize, @Nullable UUID recipe_id, @Nullable UUID user_id, @Nullable ReviewsGroups group) {
        int minRating = 0;
        int maxRating = 5;
        if (group != null)
            switch (group) {
                case All -> {
                }
                case Positive -> minRating = 4;

                case Negative -> maxRating = 3;
            }


        Integer finalMinRating = minRating;
        Integer finalMaxRating = maxRating;
        var reviews = _reviews.findAll().stream().filter(it ->
                (user_id == null || it.getUser().getId().equals(user_id)) &&
                        (recipe_id == null || it.getRecipe().getId().equals(recipe_id)) &&
                        (group == null || (it.getRating() >= finalMinRating && it.getRating() <= finalMaxRating))
        ).toList();

        return Paginator.paginate(reviews, page, pageSize);
    }

    @Override
    public ReviewEntity getReviewByID(UUID reviewID) {
        return _reviews.findById(reviewID).orElse(null);
    }

    @Override
    public ReviewEntity addReview(ReviewEntity review) {
        return _reviews.save(review);
    }

    @Override
    public ReviewEntity editReview(ReviewEntity review) {
        return _reviews.save(review);
    }

    @Override
    public void deleteReview(UUID reviewID) {
        var review = _reviews.findById(reviewID).orElse(null);
        if (review == null)
            return;
        if (review.getDeleted())
            confirmDeleteReview(reviewID);
        else {
            review.setDeleted(true);
            _reviews.save(review);
        }
    }

    @Override
    public void deleteReviews(List<UUID> reviewIDs) {
        reviewIDs.forEach(this::deleteReview);
    }

    @Override
    public void confirmDeleteReview(UUID reviewID) {
        _reviews.deleteById(reviewID);
    }

    @Override
    public void confirmDeleteReviews(List<UUID> reviewIDs) {
        _reviews.deleteAllById(reviewIDs);
    }
}
