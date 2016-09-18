package com.alisharabiani;

/**
 * A record model for the password hint record.
 */
public class RecordModel {

    /**
     * id of the record.
     */
    private int id;

    /**
     * Service name.
     */
    private String accountName;

    /**
     * Username.
     */
    private String username;

    /**
     * Password hint.
     */
    private String passwordHint;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHint() {
        return passwordHint;
    }

    public void setPasswordHint(String passwordHint) {
        this.passwordHint = passwordHint;
    }


}
