package com.example.mainprojectweatherapp;

public class ForecastModel {
        private String imageUrl;
        private String date;
        private String time;
        private String temperature;


        public ForecastModel(String imageUrl, String dayTv, String time, String temperature) {
            this.imageUrl = imageUrl;
            this.date = dayTv;
            this.time = time;
            this.temperature = temperature;

        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getDay() {
            return date;
        }

        public String getTime() {
            return time;
        }

        public  String getTemperature(){return temperature;}

    }

