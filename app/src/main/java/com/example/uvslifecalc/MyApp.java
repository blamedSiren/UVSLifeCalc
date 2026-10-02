package com.example.uvslifecalc;

import android.app.Application;

public class MyApp extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        AppColors.init(this);
    }
}
