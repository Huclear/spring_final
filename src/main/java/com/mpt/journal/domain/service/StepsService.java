package com.mpt.journal.domain.service;

import com.mpt.journal.domain.entity.StepEntity;
import com.mpt.journal.domain.model.Measure;
import com.mpt.journal.domain.model.PagedResult;

import java.util.List;
import java.util.UUID;

public interface StepsService {
    PagedResult<StepEntity> getStepsList(
            int page,
            int pageSize,
            Integer fromOrder,
            Integer toOrder,
            Double timeFrom,
            Double timeTo
    );

    PagedResult<StepEntity> getStepsByRecipe(
            int page,
            int pageSize,
            UUID recipe_ID,
            Integer fromOrder,
            Integer toOrder,
            Double timeFrom,
            Double timeTo
    );

    StepEntity getStepByID(UUID stepID);

    StepEntity addStep(StepEntity step);

    StepEntity editStep(StepEntity step);

    void deleteStep(UUID stepID);

    void deleteSteps(List<UUID> stepIDs);

    void confirmDeleteStep(UUID stepID);

    void confirmDeleteSteps(List<UUID> stepIDs);
}
