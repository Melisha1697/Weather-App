package com.example.mainprojectweatherapp;

public class DataModel {
 private String city;
 private String search;
 private String date;
 private String description;
 private String icon;
 private String temp;
 private String pressure;
 private String humidity;
 private String wind;

 public DataModel (String date, String search, String city,
                   String icon, String description,
                   String wind, String temp,String pressure,
                   String humidity){
  this.city= city;
  this.search = search;
  this.date = date;
  this.description = description;
  this.icon= icon;
  this.temp=temp;
  this.pressure = pressure;
  this.humidity=humidity;
  this.wind = wind;
 }

 public DataModel(int weatherIconId, String cityName, String mainTemp, String description) {
 }

 public String getCity(){return city;}
 public String getSearch(){return search;}
 public String getDate(){return date;}
 public String getDescription(){return description;}
 public String getIcon(){return icon;}
 public String getTemp(){return temp;}
 public String getPressure(){return  pressure;}
 public String getHumidity(){return humidity;}
 public String getWind(){return wind;}
}
