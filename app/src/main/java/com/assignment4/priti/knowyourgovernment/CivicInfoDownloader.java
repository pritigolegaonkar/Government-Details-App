package com.assignment4.priti.knowyourgovernment;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by  priti on 11/3/2018
 */
public class CivicInfoDownloader extends android.os.AsyncTask<String, Void, String> {

    private static final String TAG = "AsnycTask";
    private MainActivity mainActivity;
    private String apiURL = " https://www.googleapis.com/civicinfo/v2/representatives";
    private ArrayList<OfficialDetails> officialDetailsArrayList;
    private Object[] objects;
    String cityName, zipcode;
    String firstObject="";
    String apiKey = "AIzaSyAwW-tsSgayc9zVofCJwdQpk3ON1ov3EYE";

    public CivicInfoDownloader(MainActivity activity) {
        mainActivity = activity;
    }

    @Override
    protected String doInBackground(String... strings) {
        zipcode = strings[0];
        Uri.Builder builder = Uri.parse(apiURL).buildUpon();
        builder.appendQueryParameter("key", apiKey);
        builder.appendQueryParameter("address", zipcode);
        String completeURL = builder.build().toString();
        StringBuilder stringbuilder = new StringBuilder();
        try {
            URL url = new URL(completeURL);
            Log.d(TAG, "doInBackground: " + completeURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream inputsteam = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputsteam));

            String line;
            while ((line = reader.readLine()) != null) {
                stringbuilder.append(line).append("\n");
            }

        }
        catch(FileNotFoundException e)
        {
            Log.d(TAG, "doInBackground: File Not Found Exception");
            e.printStackTrace();
            return "";
        }
        catch(Exception e) {
            Log.d(TAG, "doInBackground: Connection not Established");
            e.printStackTrace();
            return null;
        }
        return stringbuilder.toString();
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute: " + s);
        super.onPostExecute(s);

        if (s == null) {
            Toast.makeText(mainActivity, "Civil Info Service is Unavailable", Toast.LENGTH_SHORT).show();
            mainActivity.setOfficialList(null);
            return;
        }
        if(s.equals(""))
        {
            Toast.makeText(mainActivity, " No data is available for the specified location", Toast.LENGTH_SHORT).show();
            mainActivity.setOfficialList(null);
            return;
        }

        createObject(s);

    }

    public void createObject(String s){

        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject normalizedInput = jsonObject.getJSONObject("normalizedInput");
            String city= normalizedInput.getString("city");
            String state = normalizedInput.getString("state");
            String zip = normalizedInput.getString("zip");
            if(!city.equals(""))
                firstObject = firstObject + city + ", ";
            firstObject = firstObject + state + " ";
            firstObject = firstObject + zip;
            JSONArray officesJsonArray = jsonObject.getJSONArray("offices");
            JSONArray officialsJsonArray =  jsonObject.getJSONArray("officials");
            officialDetailsArrayList= new ArrayList<>();
            for (int i = 0; i < officesJsonArray.length(); i++) {
                JSONObject officesJSONObject = (JSONObject) officesJsonArray.get(i);
                String officeName = officesJSONObject.getString("name");
                JSONArray officialIndexArray =officesJSONObject.getJSONArray("officialIndices");
                for (int j = 0; j < officialIndexArray.length(); j++) {
                    int index = officialIndexArray.getInt(j);
                    JSONObject officialJsonObject = (JSONObject) officialsJsonArray.get(index);
                    String personName = officialJsonObject.getString("name");
                    String partyName = "";
                    if (officialJsonObject.has("party"))
                        partyName = officialJsonObject.getString("party");

                    String totalAddress = "";
                    if (officialJsonObject.has("address"))
                    {
                        JSONArray address_array = (JSONArray) officialJsonObject.getJSONArray("address");
                        for (int k = 0; k < address_array.length(); k++) {
                            JSONObject jsonObject2 = (JSONObject) address_array.get(k);
                            if (jsonObject2.has("line1"))
                                totalAddress = totalAddress + jsonObject2.get("line1") + ",";
                            if (jsonObject2.has("line2"))
                                totalAddress = totalAddress + jsonObject2.get("line2") + ",";
                            if (jsonObject2.has("line3"))
                                totalAddress = totalAddress + jsonObject2.get("line3") + ",";
                            if (jsonObject2.has("city"))
                                totalAddress = totalAddress + jsonObject2.get("city") + ",";
                            if (jsonObject2.has("state"))
                                totalAddress = totalAddress + jsonObject2.get("state") + " ";
                            if (jsonObject2.has("zip"))
                                totalAddress = totalAddress + jsonObject2.get("zip") + "";
                            break;
                        }

                    }
                    String contactNumber = "No Data Provided";
                    if (officialJsonObject.has("phones")) {
                        JSONArray contactJSONArray = officialJsonObject.getJSONArray("phones");
                        contactNumber = contactJSONArray.getString(0);
                    }
                    String webpageURL = "No Data Provided";
                    if (officialJsonObject.has("urls")) {
                        JSONArray webpageURLJSONArray = officialJsonObject.getJSONArray("urls");
                        webpageURL = webpageURLJSONArray.getString(0);
                    }
                    String emailID = "No Data Provided";
                    if (officialJsonObject.has("emails")) {
                        JSONArray emailDJSONArray = officialJsonObject.getJSONArray("emails");
                        emailID = emailDJSONArray.getString(0);
                    }
                    String photoURL = "";
                    if (officialJsonObject.has("photoUrl")) {
                        photoURL = officialJsonObject.getString("photoUrl");
                    }

                    String googleplusURL = "No Data Provided";
                    String facebookURL = "No Data Provided";
                    String twitterURL = "No Data Provided";
                    String youtubeURL = "No Data Provided";

                    if(officialJsonObject.has("channels"))
                    {
                        JSONArray channel_json = officialJsonObject.getJSONArray("channels");
                        for (int o = 0; o < channel_json.length(); o++) {
                            JSONObject channel_object = (JSONObject) channel_json.get(o);
                            if (channel_object.has("type") && (channel_object.getString("type").equals("GooglePlus")))
                                googleplusURL = channel_object.getString("id");
                            else if (channel_object.has("type") && (channel_object.getString("type").equals("Facebook")))
                                facebookURL = channel_object.getString("id");
                            else if (channel_object.has("type") && (channel_object.getString("type").equals("Twitter")))
                                twitterURL = channel_object.getString("id");
                            else if (channel_object.has("type") && (channel_object.getString("type").equals("YouTube")))
                                youtubeURL = channel_object.getString("id");
                        }
                    }
                    OfficialDetails officialDetails= new OfficialDetails();
                    officialDetails.setPersonName(personName);
                    officialDetails.setOfficeName(officeName);
                    officialDetails.setPartyName(partyName);
                    officialDetails.setAddressDetails(totalAddress);
                    officialDetails.setContactNumber(contactNumber);
                    officialDetails.setWebpageURL(webpageURL);
                    officialDetails.setEmailID(emailID);
                    officialDetails.setPhotoURL(photoURL);
                    officialDetails.setCityName(city);
                    officialDetails.setStateName(state);
                    officialDetails.setZipcode(zip);
                    officialDetails.setGoogleplusURL(googleplusURL);
                    officialDetails.setFacebookURL(facebookURL);
                    officialDetails.setTwitterURL(twitterURL);
                    officialDetails.setYoutubeURL(youtubeURL);
                    officialDetailsArrayList.add(officialDetails);
                }
            }
        }
        catch (Exception e) {

            Log.d(TAG, "createObject: Error while parsing JSON data");
            e.printStackTrace();
            mainActivity.setOfficialList(null);
            return;
        }

        objects = new Object[2];
        objects[0] = firstObject;
        objects[1] = officialDetailsArrayList;
        mainActivity.setOfficialList(objects);
        return;
    }


}
