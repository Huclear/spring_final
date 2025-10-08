package com.mpt.journal.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.sql.Date;
import java.util.UUID;

@Entity(name = "ban_list")
public class BanList {
    @Id
    @Column(name = "user_id")
    private UUID id;

    @FutureOrPresent
    private Date ban_expired;

    @NotNull
    private boolean isPermanent = false;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public BanList(){}

    public boolean isPermanent() {
        return isPermanent;
    }

    public void setPermanent(boolean permanent) {
        isPermanent = permanent;
    }

    public Date getBan_expired() {
        return ban_expired;
    }

    public void setBan_expired(Date ban_expired) {
        this.ban_expired = ban_expired;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
