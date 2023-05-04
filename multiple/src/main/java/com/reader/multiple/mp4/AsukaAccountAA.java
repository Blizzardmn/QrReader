package com.reader.multiple.mp4;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.content.Context;
import android.os.Bundle;

public class AsukaAccountAA extends AbstractAccountAuthenticator {
    public AsukaAccountAA(Context context) {
        super(context);
    }

    @Override // android.accounts.AbstractAccountAuthenticator
    public Bundle addAccount(AccountAuthenticatorResponse accountAuthenticatorResponse, String str, String str2, String[] strArr, Bundle bundle) {
        return null;
    }

    @Override // android.accounts.AbstractAccountAuthenticator
    public Bundle confirmCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, Bundle bundle) {
        return null;
    }

    @Override // android.accounts.AbstractAccountAuthenticator
    public Bundle editProperties(AccountAuthenticatorResponse accountAuthenticatorResponse, String str) {
        return null;
    }

    @Override // android.accounts.AbstractAccountAuthenticator
    public Bundle getAuthToken(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String str, Bundle bundle) {
        return null;
    }

    @Override // android.accounts.AbstractAccountAuthenticator
    public String getAuthTokenLabel(String str) {
        return null;
    }

    @Override // android.accounts.AbstractAccountAuthenticator
    public Bundle hasFeatures(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String[] strArr) {
        return null;
    }

    @Override // android.accounts.AbstractAccountAuthenticator
    public Bundle updateCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String str, Bundle bundle) {
        return null;
    }
}
