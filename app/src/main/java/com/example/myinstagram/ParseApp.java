package com.example.myinstagram;

import android.app.Application;

import com.example.myinstagram.model.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Post.class);//must have this for Parse to be configured fully
        //set up Parse stuff, values straight from setup for the project on heroku website
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("MyInstagram")
                .clientKey("BBrr11@#")
                .server("http://bradc117-fbu-instagram.herokuapp.com/parse")
                .build();
        Parse.initialize(configuration);
    }
}
