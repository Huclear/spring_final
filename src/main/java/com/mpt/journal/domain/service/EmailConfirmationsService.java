package com.mpt.journal.domain.service;

import com.mpt.journal.domain.entity.EmailConfirmationEntity;
import com.mpt.journal.domain.model.PagedResult;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

public interface EmailConfirmationsService {

    PagedResult<EmailConfirmationEntity> getEmailConfirmationsList(
            int page,
            int pageSize,
            String email,
            Date date_expired_from,
            Date date_expired_to,
            Boolean showDeleted
    );

    EmailConfirmationEntity getEmailConfirmationsByUser(
            UUID user_ID
    );

    EmailConfirmationEntity getEmailConfirmationByID(UUID emailConfID);

    EmailConfirmationEntity addEmailConfirmation(EmailConfirmationEntity emailConf);

    EmailConfirmationEntity editEmailConfirmation(EmailConfirmationEntity emailConf);

    void deleteEmailConfirmation(UUID emailConfID);

    void deleteEmailConfirmations(List<UUID> emailConfIDs);

    void confirmDeleteEmailConfirmation(UUID emailConfID);

    void confirmDeleteEmailConfirmations(List<UUID> emailConfIDs);
}
