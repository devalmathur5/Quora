package com.upgrad.quora.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

@Entity
@Table(name = "user_auth", schema = "public")
@NamedQueries(
        {
                @NamedQuery(name = "userByAuthtoken", query = "select ua from UserAuthEntity ua where ua.accessToken = :accesstoken"),
        }
)
public class UserAuthEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @Size(max = 200)
    @NotNull
    private String uuid;

    @Column(name = "USER_ID")
    @NotNull
    private Integer userId;

    @Column(name = "ACCESS_TOKEN")
    @NotNull
    @Size(max = 500)
    private String accessToken;

    @Column(name = "EXPIRES_AT")
    @NotNull
    private ZonedDateTime expiresAt;

    @Column(name = "LOGIN_AT")
    @NotNull
    private ZonedDateTime loginAt;

    @Column(name = "LOGOUT_AT")
    private ZonedDateTime logoutAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public ZonedDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(ZonedDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public ZonedDateTime getLoginAt() {
        return loginAt;
    }

    public void setLoginAt(ZonedDateTime loginAt) {
        this.loginAt = loginAt;
    }

    public ZonedDateTime getLogoutAt() {
        return logoutAt;
    }

    public void setLogoutAt(ZonedDateTime logoutAt) {
        this.logoutAt = logoutAt;
    }
}
