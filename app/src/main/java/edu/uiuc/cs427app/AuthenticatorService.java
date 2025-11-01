package edu.uiuc.cs427app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticatorService extends Service {

    private AccountAuthenticator mAuthenticator;

    @Override
    // Called when the service is first created.
    public void onCreate() {
        mAuthenticator = new AccountAuthenticator(this);
    }

    @Override
    // Returns the IBinder for the AccountAuthenticator.
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
