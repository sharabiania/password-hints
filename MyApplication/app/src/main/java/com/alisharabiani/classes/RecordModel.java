package com.alisharabiani.classes;

import com.alisharabiani.R;

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
    private String serviceName;

    /**
     * Username.
     */
    private String accountName;

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

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String accountName) {
        this.serviceName = accountName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String username) {
        this.accountName = username;
    }

    public String getPasswordHint() {
        return passwordHint;
    }

    public void setPasswordHint(String passwordHint) {
        this.passwordHint = passwordHint;
    }

    public int getIcon() {
        if(serviceName == null || String.valueOf(serviceName) == "")
            return -1;

        switch (serviceName.toLowerCase()){
            case "amazon":
                return R.drawable.amazon;

            case "bitbucket":
                return R.drawable.bitbucket;

            case "ea games":
            case "electronic arts":
                return R.drawable.eagames;

            case "ebay":
                return R.drawable.ebay;

            case "facebook":
                return R.drawable.facebook;

            case "github":
                return R.drawable.github;

            case "gmail":
                return R.drawable.gmail;

            case "godaddy":
                return R.drawable.godaddy;

            case "google":
                return R.drawable.google;

            case "ibm":
                return R.drawable.ibm;

            case "instagram":
                return R.drawable.instagram;

            case "microsoft":
                return R.drawable.microsoft;

            case "msdn":
                return R.drawable.msdn;

            case "msn":
                return R.drawable.msn;

            case "oracle":
                return R.drawable.oracle;

            case "rbc bank":
                return R.drawable.rbc;

            case "scotiabank":
            case "scotia bank":
                return R.drawable.scotiabank;

            case "stackoverflow":
                return R.drawable.stackoverflow;

            case "td bank":
                return R.drawable.tdbank;

            case "twitter":
                return R.drawable.twitter;

            case "ubisoft":
                return R.drawable.ubisoft;

            default:
                return R.drawable.ic_lock_lock_alpha;
        }
    }

}
