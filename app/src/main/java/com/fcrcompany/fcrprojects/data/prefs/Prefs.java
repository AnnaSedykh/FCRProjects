package com.fcrcompany.fcrprojects.data.prefs;

public interface Prefs {

    String getToken();

    void setToken(String token);

    String getAccountName();

    void setAccountName(String accountName);

    boolean isAccessRequestSent();

    void setAccessRequestSent(boolean isSent);

    void setAccountChooserOnTop(boolean isOnTop);

    boolean isAccountChooserOnTop();
}
