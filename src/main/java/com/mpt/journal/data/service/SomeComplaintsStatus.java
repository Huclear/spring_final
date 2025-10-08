package com.mpt.journal.data.service;

import com.mpt.journal.data.Paginator;
import com.mpt.journal.domain.entity.ComplaintEntity;
import com.mpt.journal.domain.model.ComplaintStatus;
import com.mpt.journal.domain.model.PagedResult;
import com.mpt.journal.domain.repository.ComplaintsRepository;
import com.mpt.journal.domain.service.ComplaintsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SomeComplaintsStatus implements ComplaintsService {

    private ComplaintsRepository _complaints;

    public SomeComplaintsStatus(ComplaintsRepository complaints) {
        _complaints = complaints;
    }

    @Override
    public PagedResult<ComplaintEntity> getComplaintsList(int page, int pageSize, UUID recipe_ID, UUID user_ID, String topic, ComplaintStatus status) {
        var complaints = _complaints.findAll().stream().filter(it ->
                (user_ID == null || (it.getUser() != null && it.getUser().getId() == user_ID)) &&
                        (recipe_ID == null || (it.getRecipe() != null && it.getRecipe().getId() == recipe_ID)) &&
                        (topic == null || it.getTopic().contains(topic)) &&
                        (status == null || it.getStatus() == status)).toList();

        return Paginator.paginate(complaints, page, pageSize);
    }

    @Override
    public ComplaintEntity getComplaintByID(UUID complaintID) {
        return _complaints.findById(complaintID).orElse(null);
    }

    @Override
    public ComplaintEntity addComplaint(ComplaintEntity complaint) {
        return _complaints.save(complaint);
    }

    @Override
    public ComplaintEntity editComplaint(ComplaintEntity complaint) {
        return _complaints.save(complaint);
    }

    @Override
    public void deleteComplaint(UUID complaintID) {
        _complaints.deleteById(complaintID);
    }

    @Override
    public void deleteComplaints(List<UUID> complaintIDs) {
        _complaints.deleteAllById(complaintIDs);
    }
}
