package com.example.battleship.models;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.Date;

public class SessionInfo {
    public Date startTime;
    public UserProfile account;

    public SessionInfo(GoogleSignInAccount account) {
        this.startTime = new Date();
        this.account = new UserProfile(account.getDisplayName(), account.getAccount(), account.getEmail(), account.getFamilyName(), account.getGivenName(), account.getPhotoUrl(), account.getServerAuthCode());
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public UserProfile getAccount() {
        return account;
    }

    public void setAccount(UserProfile account) {
        this.account = account;
    }
}
