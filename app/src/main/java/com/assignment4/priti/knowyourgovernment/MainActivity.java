package com.assignment4.priti.knowyourgovernment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



public class MainActivity extends AppCompatActivity implements RecyclerItemClickListener.OnRecyclerClickListener {

    private static final String TAG = "MainActivity";
    private TextView locationEntered;
    private RecyclerView recyclerView;
    private ArrayList<OfficialDetails> officialDetailsArrayList = new ArrayList<>();
    private OfficialDetailsAdapter officialDetailsAdapter;
    private Locator locator;
    private CivicInfoDownloader civicInfoDownloader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        locationEntered = findViewById(R.id.cityStateZip);
        recyclerView = findViewById(R.id.recylerView);
        officialDetailsAdapter = new OfficialDetailsAdapter(this, officialDetailsArrayList);
        recyclerView.setAdapter(officialDetailsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        if(isInternetConnected()){
            locator = new Locator(this);
        } else {
            Log.d(TAG, "onCreate: network connectivity");
            locationEntered.setText("No Data For Location");
            android.app.AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Data cannot be accessed/loaded without an internet connection");
            builder.setTitle("No Network Connection");
            android.app.AlertDialog dialog = builder.create();
            dialog.show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.homeAsUp:
                super.onBackPressed();
                return true;
            case R.id.searchLocation:
                Log.d(TAG, "onOptionsItemSelected: Search Location");
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setGravity(Gravity.CENTER_HORIZONTAL);
                input.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String enteredInput =  input.getText().toString();
                        if(!enteredInput.equals("")) {
                            civicInfoDownloader = new CivicInfoDownloader(MainActivity.this);
                            civicInfoDownloader.execute(enteredInput);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.setMessage("Enter a City,State or Zip Code:");
               final AlertDialog dialog = builder.create();
                input.setOnKeyListener(new View.OnKeyListener()
                {
                    public boolean onKey(View v, int keyCode, KeyEvent event)
                    {
                        if (event.getAction() == KeyEvent.ACTION_DOWN)
                        {
                            switch (keyCode)
                            {
                                case KeyEvent.KEYCODE_DPAD_CENTER:
                                case KeyEvent.KEYCODE_ENTER:
                                    if(input.getText() != null)
                                        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
                                    else
                                        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).performClick();
                                    return true;
                                default:
                                    break;
                            }
                        }
                        return false;
                    }
                });
                dialog.show();
                break;
            case R.id.aboutApp:
                Log.d(TAG, "onOptionsItemSelected:About ");
                Intent intent = new Intent(this, AboutActivity.class);
                intent.putExtra("appName", "Know Your Government");
                intent.putExtra("author", " ©  2018 ,  Priti Golegaonkar");
                startActivity(intent);
                break;
        }

        return true;
    }

    private boolean isInternetConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onResume() {

        /*if (isInternetConnected())
        {
            if(locator==null)
            {
                locator=new Locator(this);
            }
        }*/
        super.onResume();
    }

    @Override
    public void onItemClick(View v, int pos) {

        Log.d(TAG, "onItemClick: ");
        Intent intent= new Intent(MainActivity.this,OfficerActivity.class);
        String heading=locationEntered.getText().toString();
        intent.putExtra("location",heading);
        final int position=recyclerView.getChildLayoutPosition(v);
        intent.putExtra("details",officialDetailsArrayList.get(position));
        Log.d(TAG, "onItemClick: " + heading);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onLongItemClick(View v, int pos) {
        onItemClick(v, pos);
    }

    public void doLocationWork(double latitude, double longitude){

        Log.d(TAG, "doLocationWork: Lat: " + latitude + ", Lon: " + longitude);
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addressList = null;
        try {
            addressList = geocoder.getFromLocation(latitude, longitude, 1);
            civicInfoDownloader = new CivicInfoDownloader(this);
            civicInfoDownloader.execute( addressList.get(0).getPostalCode());
        } catch (IOException e){
            Toast.makeText(this, " address cannot be acquired from provided latitude/longitude", Toast.LENGTH_SHORT).show();
        }
    }

    public void setOfficialList(Object[] objects) {


        if (objects == null) {
            locationEntered.setText("No Data For Location");
            officialDetailsArrayList.clear();
        } else {
            String locationText = (String)objects[0];
            Log.d(TAG, "setOfficialList: " + locationText);
            officialDetailsArrayList.clear();
            officialDetailsArrayList.addAll((ArrayList<OfficialDetails>) objects[1]);
            locationEntered.setText(locationText);
            Log.d(TAG, "setOfficialList: " + objects[1].toString());
        }

        officialDetailsAdapter.notifyDataSetChanged();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 9090){
            if (grantResults.length == 0) {
                Log.d(TAG, "onRequestPermissionsResult: Permissions are not granted");
                return;
            }
            for(int i=0 ; i < permissions.length;i++)
            {
                if(permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        locator.determineLocation();
                        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                    }
                    else {
                        Toast.makeText(this, "Address cannot be acquired from provided latitude/longitude", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }


    public void noLocationAvailable(){
        Toast.makeText(this, "Unable to get location from any location provider", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
