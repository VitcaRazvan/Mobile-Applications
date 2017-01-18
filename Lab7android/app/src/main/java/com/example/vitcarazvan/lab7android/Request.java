package com.example.vitcarazvan.lab7android;

/**
 * Created by Vitca Razvan on 1/17/2017.
 */

public class Request {
    private String tagName;
    private String city;
    private String sprayCan;
    private String status;
    private String email;
    private String key;

    //constructor
    public Request(String tagNameParam, String cityParam , String sprayCanParam){
        this.tagName = tagNameParam;
        this.city = cityParam;
        this.sprayCan = sprayCanParam;
        this.status = "pending";
        this.key="";

    }

    //getters-setters
    public String getTagName(){return this.tagName;}
    public String getCity(){return this.city;}
    public String getSprayCan(){return this.sprayCan;}
    public String getEmail(){return this.email;}
    public String getStatus(){return this.status;}
    public String getKey(){return this.key;}



    public void setTagName(String tagName){this.tagName = tagName;}
    public void setAddress(String city){this.city = city;}
    public void setSprayCan(String sprayCan){this.sprayCan = sprayCan;}
    public void setKey(String key){this.key = key;}
    public void setStatus(String status){this.status = status;}
    public void setEmail(String email){this.email = email;}
    // to string
    public String toString(){
        return "Tag Name: " + this.tagName + "\nCity: " + this.city + "\nSpray Can: " + this.sprayCan ;
    }
}
