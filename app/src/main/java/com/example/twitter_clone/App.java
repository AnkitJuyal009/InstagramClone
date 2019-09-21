package com.example.twitter_clone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("b4AmnAo0Gg5mV8ytCPOQ7nwvDBKq7Yynhhuzh32N")
                // if defined
                .clientKey("gISLlvQMdLG11pj7lNamPsfDtXEV9zkPmlrHeDmz")
                .server("https://parseapi.back4app.com/")
                .build()
        );

    }
}
