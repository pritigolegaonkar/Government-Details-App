package com.assignment4.priti.knowyourgovernment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by  priti on 11/3/2018
 */
public class AboutActivity extends AppCompatActivity {
    TextView appName;
    TextView authorName;
    TextView versionNumber;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
      appName=findViewById(R.id.appName);
        authorName=findViewById(R.id.authorName);
        versionNumber=findViewById(R.id.version);
        String year = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
        Intent intent=getIntent();
        String name=intent.getStringExtra("appName");
        String author=intent.getStringExtra("author");
        String version="Version " + BuildConfig.VERSION_NAME.toString();
        appName.setText(name);
        authorName.setText(author);
        versionNumber.setText(version);

    }
}
