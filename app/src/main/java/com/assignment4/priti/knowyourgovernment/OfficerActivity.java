package com.assignment4.priti.knowyourgovernment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


/**
 * Created by  priti on 11/3/2018
 */
public class OfficerActivity extends AppCompatActivity  {

    private static final String TAG = "OfficerActivity";

    TextView  locationText;
    OfficialDetails details;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate: OfficerActivity");
        setContentView(R.layout.activity_officer);

        locationText = findViewById(R.id.location);

        TextView personName = findViewById(R.id.personNameView);
        TextView officeName =findViewById(R.id.officeView);
        TextView partyName =findViewById(R.id.partyView);
        TextView address =  findViewById(R.id.addressView);
        TextView contact =findViewById(R.id.contactView);
        TextView emailID =  findViewById(R.id.emailView);
        TextView webpageURL =findViewById(R.id.webpageView);
        ScrollView scrollview = findViewById(R.id.scrollViewOfficerInfo);
        ImageView google = findViewById(R.id.googleplus);
        ImageView twitter =  findViewById(R.id.twitter);
        ImageView facebook =  findViewById(R.id.facebook);
        ImageView youtube = findViewById(R.id.youtube);
        final ImageView photo = findViewById(R.id.photoView);

        Intent intent = getIntent();
        locationText.setText(intent.getStringExtra("location"));
        details = (OfficialDetails) intent.getSerializableExtra("details");
        personName.setText(details.getPersonName());
        officeName.setText(details.getOfficeName());
        partyName.setText("(" + details.getPartyName() + ")");
        address.setText(details.getAddressDetails());
        contact.setText(details.getContactNumber());
        emailID.setText(details.getEmailID());
        webpageURL.setText(details.getWebpageURL());
        if (details.getPartyName().equalsIgnoreCase("Republican"))
            scrollview.setBackgroundColor(Color.RED);
        else if (details.getPartyName().equalsIgnoreCase("Democratic") || details.getPartyName().equalsIgnoreCase("Democrat") || details.getPartyName().equalsIgnoreCase("Democratic Party"))
            scrollview.setBackgroundColor(Color.BLUE);
        else
            scrollview.setBackgroundColor(Color.BLACK);
        if (details.getGoogleplusURL().equals("No Data Provided"))
            google.setVisibility(View.INVISIBLE);
        if (details.getFacebookURL().equals("No Data Provided"))
            facebook.setVisibility(View.INVISIBLE);
        if (details.getTwitterURL().equals("No Data Provided"))
            twitter.setVisibility(View.INVISIBLE);
        if (details.getYoutubeURL().equals("No Data Provided"))
            youtube.setVisibility(View.INVISIBLE);
        if (!address.getText().toString().equals("No Data Provided"))
            Linkify.addLinks(address, Linkify.MAP_ADDRESSES);
        if (!contact.getText().toString().equals("No Data Provided"))
            Linkify.addLinks(contact, Linkify.PHONE_NUMBERS);
        if (!emailID.getText().toString().equals("No Data Provided"))
            Linkify.addLinks(emailID, Linkify.EMAIL_ADDRESSES);
        if (!webpageURL.getText().toString().equals("No Data Provided"))
            Linkify.addLinks(webpageURL, Linkify.WEB_URLS);

        if (details != null) {
            if(!details.getPhotoURL().equals("")){
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

            photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!details.getPhotoURL().equals("")){
                        openPhotoActivity(v);
                    } else {
                        Toast.makeText(OfficerActivity.this, "Image not found!!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }


    public void openPhotoActivity(View v){
        if (details == null || details.getPhotoURL().equals(""))
            return;

        Intent intent= new Intent(OfficerActivity.this,PhotoActivity.class);
        String heading=locationText.getText().toString();
        intent.putExtra("location",heading);
        intent.putExtra("details",details);
        startActivityForResult(intent, 1);



    }


    public void googlePlusClicked(View v) {
        if (details == null || details.getGoogleplusURL().equals(""))
            return;

        String name = details.getGoogleplusURL();
        Intent intent1 = null;
        try {
            intent1 = new Intent(Intent.ACTION_VIEW);
            intent1.setClassName("com.google.android.apps.plus",
                    "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent1.putExtra("customAppUri", name);
            startActivity(intent1);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://plus.google.com/" + name)));
        }
    }

    public void youTubeClicked(View v) {
        if (details == null || details.getYoutubeURL().equals(""))
            return;

        String name = details.getYoutubeURL();
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/" + name)));
        }
    }

    public void twitterClicked(View v) {
        if (details == null || details.getTwitterURL().equals(""))
            return;

        Intent intent = null;
        String name = details.getTwitterURL();
        try {
            // get the Twitter app if possible
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
        }
        startActivity(intent);
    }

    public void facebookClicked(View v) {
        if (details == null || details.getFacebookURL().equals(""))
            return;

        String FACEBOOK_URL = "https://www.facebook.com/" + details.getFacebookURL();
        String urlToUse;
        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                //urlToUse = "fb://page/" + channels.get("Facebook");
                urlToUse = "fb://page/" + details.getFacebookURL();
            }
        } catch (PackageManager.NameNotFoundException e) {
            urlToUse = FACEBOOK_URL; //normal web url
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(urlToUse));
        startActivity(facebookIntent);

    }


}





