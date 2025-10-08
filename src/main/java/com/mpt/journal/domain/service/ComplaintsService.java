package com.mpt.journal.domain.service;


import com.mpt.journal.domain.entity.ComplaintEntity;
import com.mpt.journal.domain.model.ComplaintStatus;
import com.mpt.journal.domain.model.PagedResult;

import java.util.List;
import java.util.UUID;

public interface ComplaintsService 
{
    PagedResult<ComplaintEntity> getComplaintsList(
            int page,
            int pageSize,
            UUID recipe_ID,
            UUID user_ID,
            String topic,
            ComplaintStatus status
    );

    ComplaintEntity getComplaintByID(UUID complaintID);

    ComplaintEntity addComplaint(ComplaintEntity complaint);

    ComplaintEntity editComplaint(ComplaintEntity complaint);

    void deleteComplaint(UUID complaintID);

    void deleteComplaints(List<UUID> complaintIDs);
}
