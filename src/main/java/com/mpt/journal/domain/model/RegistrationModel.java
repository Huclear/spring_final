package com.mpt.journal.domain.model;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegistrationModel {


    @Pattern(regexp = "^(?=.*[#?!@$%^&*_+`-])(?=.*[A-Za-z])(?=.*[0-9]).{8,}$")
    private String login;

    @Pattern(regexp = "^(?=.*[#?!@$%^&*_+`-])(?=.*[A-Za-z]).{5,}$")
    private String nickname;

    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*_+`-]).{8,}$")
    private String password;

    @NotBlank
    private String aboutMe;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }
}
