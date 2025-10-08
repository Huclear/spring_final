package com.mpt.journal.data.service;

import com.mpt.journal.data.Paginator;
import com.mpt.journal.domain.entity.StepEntity;
import com.mpt.journal.domain.model.PagedResult;
import com.mpt.journal.domain.repository.StepsRepository;
import com.mpt.journal.domain.service.StepsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SomeStepsService implements StepsService {

    private final StepsRepository _steps;

    public SomeStepsService(StepsRepository steps) {
        _steps = steps;
    }

    @Override
    public PagedResult<StepEntity> getStepsList(
            int page,
            int pageSize,
            Integer fromOrder,
            Integer toOrder,
            Double timeFrom,
            Double timeTo) {
        var steps = _steps.findAll().stream().filter(step ->
                (fromOrder == null || step.getStep_order() >= fromOrder) &&
                        (toOrder == null || step.getStep_order() <= toOrder) &&
                        (timeFrom == null || step.getDurationMinutes() >= timeFrom) &&
                        (timeTo == null || step.getDurationMinutes() <= timeTo)
        ).toList();

        return Paginator.paginate(steps, page, pageSize);
    }

    @Override
    public PagedResult<StepEntity> getStepsByRecipe(int page, int pageSize, UUID recipe_ID, Integer fromOrder, Integer toOrder, Double timeFrom, Double timeTo) {
        var steps = _steps.findAll().stream().filter(step ->
                (recipe_ID == null || step.getRecipe().getId().equals(recipe_ID)) &&
                        (fromOrder == null || step.getStep_order() >= fromOrder) &&
                        (toOrder == null || step.getStep_order() <= toOrder) &&
                        (timeFrom == null || step.getDurationMinutes() >= timeFrom) &&
                        (timeTo == null || step.getDurationMinutes() <= timeTo)
        ).toList();

        return Paginator.paginate(steps, page, pageSize);
    }

    @Override
    public StepEntity getStepByID(UUID stepID) {
        return _steps.findById(stepID).orElse(null);
    }

    @Override
    public StepEntity addStep(StepEntity step) {
        return _steps.save(step);
    }

    @Override
    public StepEntity editStep(StepEntity step) {
        return _steps.save(step);
    }

    @Override
    public void deleteStep(UUID stepID) {
        var step = _steps.findById(stepID).orElse(null);
        if (step == null)
            return;
        if (step.getDeleted())
            confirmDeleteStep(stepID);
        else {
            step.setDeleted(true);
            _steps.save(step);
        }
    }

    @Override
    public void deleteSteps(List<UUID> stepIDs) {
        stepIDs.forEach(this::deleteStep);
    }

    @Override
    public void confirmDeleteStep(UUID stepID) {
        _steps.deleteById(stepID);
    }

    @Override
    public void confirmDeleteSteps(List<UUID> stepIDs) {
        _steps.deleteAllById(stepIDs);
    }
}
