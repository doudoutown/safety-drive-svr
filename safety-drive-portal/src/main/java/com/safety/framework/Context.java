package com.safety.framework;

/**
 * Created by fanwenbin on 16/9/19.
 */
public class Context {
    private Integer accountId;
    private String username;

    public Context(Integer accountId, String username) {
        this.accountId = accountId;
        this.username = username;
    }

    public Context(String username) {
        this.username = username;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
