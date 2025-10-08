package com.mpt.journal.data.service;

import com.mpt.journal.data.Paginator;
import com.mpt.journal.domain.entity.EmailConfirmationEntity;
import com.mpt.journal.domain.model.PagedResult;
import com.mpt.journal.domain.repository.EmailConfirmationRepository;
import com.mpt.journal.domain.service.EmailConfirmationsService;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Service
public class SomeEmailConfirmationService implements EmailConfirmationsService {

    private final EmailConfirmationRepository _emails;

    public SomeEmailConfirmationService(EmailConfirmationRepository emails) {
        this._emails = emails;
    }

    @Override
    public PagedResult<EmailConfirmationEntity> getEmailConfirmationsList(int page, int pageSize, String email, Date date_expired_from, Date date_expired_to, Boolean showDeleted) {
        var response = _emails.findAll().stream().filter(e ->
                        e.getDeleted() == (showDeleted != null && showDeleted) &&
                                (email == null || email.isBlank() || e.getEmail().contains(email)) &&
                                (date_expired_from == null || e.getTokenExpired().compareTo(date_expired_from) >= 0) &&
                                (date_expired_to == null || e.getTokenExpired().compareTo(date_expired_to) <= 0)
                )
                .toList();
        return Paginator.paginate(response, page, pageSize);
    }

    @Override
    public EmailConfirmationEntity getEmailConfirmationsByUser(UUID user_ID) {
        return _emails.findAll().stream()
                .filter(e -> e.getUser() != null && e.getUser().getId() == user_ID)
                .findFirst().orElse(null);
    }

    @Override
    public EmailConfirmationEntity getEmailConfirmationByID(UUID emailConfID) {
        return _emails.findById(emailConfID).orElse(null);
    }

    @Override
    public EmailConfirmationEntity addEmailConfirmation(EmailConfirmationEntity emailConf) {
        return _emails.save(emailConf);
    }

    @Override
    public EmailConfirmationEntity editEmailConfirmation(EmailConfirmationEntity emailConf) {
        return _emails.save(emailConf);
    }

    @Override
    public void deleteEmailConfirmation(UUID emailConfID) {
        var conf = getEmailConfirmationByID(emailConfID);
        if (conf == null)
            return;
        if (conf.getDeleted())
            confirmDeleteEmailConfirmation(emailConfID);
        else {
            conf.setDeleted(true);
            _emails.save(conf);
        }
    }

    @Override
    public void deleteEmailConfirmations(List<UUID> emailConfIDs) {
        emailConfIDs.forEach(this::deleteEmailConfirmation);
    }

    @Override
    public void confirmDeleteEmailConfirmation(UUID emailConfID) {
        _emails.deleteById(emailConfID);
    }

    @Override
    public void confirmDeleteEmailConfirmations(List<UUID> emailConfIDs) {
        _emails.deleteAllById(emailConfIDs);
    }
}
