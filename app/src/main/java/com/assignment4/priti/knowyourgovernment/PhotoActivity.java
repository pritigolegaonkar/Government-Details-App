package com.assignment4.priti.knowyourgovernment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.content.Intent;
import android.graphics.Color;

import android.support.v7.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class PhotoActivity extends AppCompatActivity {

    TextView locationPhotoView;
    OfficialDetails details;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        locationPhotoView = (TextView) findViewById(R.id.locationPhotoView);
        TextView officePhotoView = findViewById(R.id.officePhotoView);
        TextView personNamePhotoView = findViewById(R.id.personNamePhotoView);
        final ImageView photo = (ImageView) findViewById(R.id.photo);
        ScrollView scrollView=(ScrollView)findViewById(R.id.scrollViewPhotoInfo);

        Intent intent1 = getIntent();
        if (intent1.hasExtra("location")) {
            locationPhotoView.setText(intent1.getStringExtra("location"));
        }
        if (intent1.hasExtra("details")) {
            details = (OfficialDetails) intent1.getSerializableExtra("details");
            officePhotoView.setText(details.getOfficeName());
            personNamePhotoView.setText(details.getPersonName());
            if (details.getPartyName().equalsIgnoreCase("Republican"))
                scrollView.setBackgroundColor(Color.RED);
            else if (details.getPartyName().equalsIgnoreCase("Democratic") || details.getPartyName().equalsIgnoreCase("Democrat"))
                scrollView.setBackgroundColor(Color.BLUE);
            else
                scrollView.setBackgroundColor(Color.BLACK);

        }

        if (details != null) {
            Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    final String changedUrl = details.getPhotoURL().replace("http:", "https:");
                    picasso.load(changedUrl)
                            .error(R.drawable.brokenimage)
                            .placeholder(R.drawable.placeholder)
                            .into(photo);
                }
            }).build();
            picasso.load(details.getPhotoURL())
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder)
                    .into(photo);


        } else {
            Picasso.with(this).load(R.drawable.missingimage)
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.missingimage)
                    .into(photo);

        }

    }
}