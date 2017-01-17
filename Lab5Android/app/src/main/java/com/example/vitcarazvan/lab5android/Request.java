package com.example.vitcarazvan.lab5android;

/**
 * Created by Vitca Razvan on 1/16/2017.
 */
// class for REQUEST sent by a customer
public class Request{
    private String tagName;
    private String city;
    private String sprayCan;



    //constructor
    public Request(String tagNameParam, String cityParam , String sprayCanParam){
        this.tagName = tagNameParam;
        this.city = cityParam;
        this.sprayCan = sprayCanParam;

    }

    //getters-setters
    public String getTagName(){return this.tagName;}
    public String getCity(){return this.city;}
    public String getSprayCan(){return this.sprayCan;}

    public void setTagName(String tagName){this.tagName = tagName;}
    public void setAddress(String city){this.city = city;}
    public void setSprayCan(String sprayCan){this.sprayCan = sprayCan;}

    // to string
    public String toString(){
        return "Tag Name: " + this.tagName + "\nCity: " + this.city + "\nSpray Can: " + this.sprayCan ;
    }
}
