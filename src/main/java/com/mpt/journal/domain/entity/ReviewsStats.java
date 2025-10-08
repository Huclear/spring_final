package com.mpt.journal.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Entity(name = "reviews_stats")
public class ReviewsStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @DecimalMin(value = "0.0")
    @Column(name = "absolute_average_rating")
    private Double absAvgRating = 0.0;

    @NotNull
    @DecimalMin(value = "0.0")
    @Column(name = "confidence_reviews_count")
    private Double confReviewsCount = 0.0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAbsAvgRating() {
        return absAvgRating;
    }

    public void setAbsAvgRating(Double absAvgRating) {
        this.absAvgRating = absAvgRating;
    }

    public Double getConfReviewsCount() {
        return confReviewsCount;
    }

    public void setConfReviewsCount(Double confReviewsCount) {
        this.confReviewsCount = confReviewsCount;
    }
}
