package com.example.a24a10357exe2.UI_Controllers;

import android.app.Application;

import com.example.a24a10357exe2.Utilities.SharedPreferencesManager;
import com.example.a24a10357exe2.Utilities.SignalManager;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesManager.init(this);
        SignalManager.init(this);
    }
}
