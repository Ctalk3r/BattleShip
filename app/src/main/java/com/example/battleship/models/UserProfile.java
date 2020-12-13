package com.example.battleship.models;

import android.accounts.Account;
import android.net.Uri;

public class UserProfile {
    private String displayName;
    private Account serviceAccount;
    private String email;
    private String familyName;
    private String givenName;
    private Uri photoUrl;
    private String serverAuthCode;

    public UserProfile(String displayName, Account serviceAccount, String email, String familyName, String givenName, Uri photoUrl, String serverAuthCode) {
        this.displayName = displayName;
        this.serviceAccount = serviceAccount;
        this.email = email;
        this.familyName = familyName;
        this.givenName = givenName;
        this.photoUrl = photoUrl;
        this.serverAuthCode = serverAuthCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Account getServiceAccount() {
        return serviceAccount;
    }

    public void setServiceAccount(Account serviceAccount) {
        this.serviceAccount = serviceAccount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public Uri getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(Uri photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getServerAuthCode() {
        return serverAuthCode;
    }

    public void setServerAuthCode(String serverAuthCode) {
        this.serverAuthCode = serverAuthCode;
    }
}
