package com.mpt.journal.domain.entity;

import com.mpt.journal.domain.model.ComplaintStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

@Entity(name = "complaints")
public class ComplaintEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private UserEntity user_sender;

    @ManyToOne(cascade = CascadeType.ALL)
    private UserEntity user;


    @ManyToOne(cascade = CascadeType.ALL)
    private RecipeEntity recipe;

    @Pattern(regexp = "^(?=.*[A-Za-z]+)(?=.*[A-Za-z 0-9]*).{2,}$")
    private String topic;

    @Pattern(regexp = "^(?=.*[A-Za-z]+)(?=.*[A-Za-z 0-9]*).{50,500}$")
    private String description;


    @NotNull
    @Enumerated(EnumType.STRING)
    private ComplaintStatus status = ComplaintStatus.Pending;

    public ComplaintStatus getStatus() {
        return status;
    }

    public void setStatus(ComplaintStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public RecipeEntity getRecipe() {
        return recipe;
    }

    public void setRecipe(RecipeEntity recipe) {
        this.recipe = recipe;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UserEntity getUser_sender() {
        return user_sender;
    }

    public void setUser_sender(UserEntity user_sender) {
        this.user_sender = user_sender;
    }
}
