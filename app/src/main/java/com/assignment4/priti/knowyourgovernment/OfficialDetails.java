package com.assignment4.priti.knowyourgovernment;

import java.io.Serializable;

/**
 * Created by  priti on 11/3/2018
 */
public class OfficialDetails implements Serializable {

    //Locatoin Details
    private String cityName;
    private String stateName;
    private String zipcode;

    //Official Details
    private String personName;
    private String officeName;
    private String partyName;

    //Personal Details
    private String addressDetails;
    private String contactNumber;
    private String webpageURL;
    private String emailID;
    private String photoURL;

    //Social weblinks
    private String googleplusURL;
    private String facebookURL;
    private String twitterURL;
    private String youtubeURL;

    //Get Methods of data
    public String getCityName(){
        return  cityName;
    }

    public String getStateName(){
        return  stateName;
    }

    public String getZipcode(){
        return  zipcode;
    }

    public String getPersonName(){
        return  personName;
    }

    public String getOfficeName(){
        return  officeName;
    }

    public String getPartyName(){
        return  partyName;
    }

    public String getAddressDetails(){
        return  addressDetails;
    }

    public String getContactNumber(){
        return  contactNumber;
    }

    public String getWebpageURL(){
        return  webpageURL;
    }

    public String getEmailID(){
        return  emailID;
    }

    public String getPhotoURL(){
        return  photoURL;
    }

    public String getGoogleplusURL(){
        return  googleplusURL;
    }

    public String getFacebookURL(){
        return  facebookURL;
    }

    public String getTwitterURL(){
        return  twitterURL;
    }

    public String getYoutubeURL(){
        return  youtubeURL;
    }

    //Set Method of data
    public  void setCityName(String value){
        this.cityName = value;
    }

    public  void setStateName(String value){
        this.stateName = value;
    }

    public  void setZipcode(String value){
        this.zipcode = value;
    }

    public  void setPersonName(String value){
        this.personName = value;
    }

    public  void setOfficeName(String value){
        this.officeName = value;
    }

    public  void setPartyName(String value){
        this.partyName = value;
    }

    public  void setAddressDetails(String value){
        this.addressDetails = value;
    }

    public  void setContactNumber(String value){
        this.contactNumber = value;
    }

    public  void setWebpageURL(String value){
        this.webpageURL = value;
    }

    public  void setEmailID(String value){
        this.emailID = value;
    }

    public  void setPhotoURL(String value){
        this.photoURL = value;
    }

    public  void setGoogleplusURL(String value){
        this.googleplusURL = value;
    }

    public  void setFacebookURL(String value){
        this.facebookURL = value;
    }

    public  void setTwitterURL(String value){
        this.twitterURL = value;
    }

    public  void setYoutubeURL(String value){
        this.youtubeURL = value;
    }

    @Override
    public String toString() {
        String AllDetails = "OfficialDetails{"+
                                        "Name='" + personName + '\'' +
                                        ", Office='" + officeName + '\'' +
                                        ", Party='" + partyName + '\'' +
                                        ", Address='" + addressDetails + '\'' +
                                        ", Contact='" + contactNumber + '\'' +
                                        ", Webpage='" + webpageURL + '\'' +
                                        ", Email='" + emailID + '\'' +
                                        ", Photo='" + photoURL + '\'' +
                                        ", City='" + cityName + '\'' +
                                        ", State='" + stateName + '\'' +
                                        ", Zipcode='" + zipcode + '\'' +
                                        ", Googleplus='" + googleplusURL + '\'' +
                                        ", Facebook='" + facebookURL + '\'' +
                                        ", Twitter='" + twitterURL + '\'' +
                                        ", Youtube='" + youtubeURL + '\'' +
                                        '}';
        return AllDetails;
    }
}

