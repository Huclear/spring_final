package com.mpt.journal.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.sql.Date;
import java.util.UUID;

@Entity(name = "email_confirmations")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"email"}))
public class EmailConfirmationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Email
    private String email;

    private String hash;
    private String salt;

    @FutureOrPresent
    private Date tokenExpired;

    @OneToOne(mappedBy = "emailConf")
    private UserEntity user;

    private Boolean deleted = false;

    @NotNull
    private Boolean confirmed = false;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Date getTokenExpired() {
        return tokenExpired;
    }

    public void setTokenExpired(Date tokenExpired) {
        this.tokenExpired = tokenExpired;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
