package com.example.mainprojectweatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mainprojectweatherapp.DataModel;
import com.example.mainprojectweatherapp.R;
import com.example.mainprojectweatherapp.RecyclerViewAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WeatherApp extends AppCompatActivity {

    private TextView cityTv, dateTv, descWeatherTv, mainTempTv, humidityTv, pressureTv, windTv;
    private ImageView mainIconTv, searchIv;
    private SearchView searchSv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_app);
        searchIv = findViewById(R.id.searchIv);
        searchSv = findViewById(R.id.searchSv);
        cityTv = findViewById(R.id.cityTv);
        dateTv = findViewById(R.id.dateTv);
        descWeatherTv = findViewById(R.id.descWeatherTv);
        mainTempTv = findViewById(R.id.mainTempTv);
        humidityTv = findViewById(R.id.humidityTv);
        pressureTv = findViewById(R.id.pressureTv);
        windTv = findViewById(R.id.windTv);
        mainIconTv = findViewById(R.id.mainIconTv);
        searchIv= findViewById(R.id.searchIv);

        String CurrentForecast = "https://api.openweathermap.org/data/2.5/weather?q=Kathmandu&appid=86efc9be8662947df3dfa0f75fc53799";
        RequestQueue requestQueue = Volley.newRequestQueue(this);


        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                CurrentForecast,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String jsonData = response;
                        Log.d("JSON DATA: ", jsonData);

                        parseWeather(jsonData);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("this is an error", error.toString());
                        View view = findViewById(R.id.recyclerView);
                        Snackbar.make(view, "Please enter the correct city name!!", Snackbar.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(stringRequest);


        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        ArrayList<ForecastModel> tasks = new ArrayList<ForecastModel>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);

        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, tasks);

//        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this,tasks);
        recyclerView.setAdapter(adapter);
        String forecast = "https://api.openweathermap.org/data/2.5/forecast?q=kathmandu&appid=86efc9be8662947df3dfa0f75fc53799";

        StringRequest forecastStringRequest = new StringRequest(
                Request.Method.GET,
                forecast,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String jsonData = response;
                        Log.d("JSON DATA(Forecast):", jsonData);

                        jsonArrayDecode(jsonData, adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(WeatherApp.this,"Something is wrong", Toast.LENGTH_SHORT).show();

            }
        });
        requestQueue.add(forecastStringRequest);

        SearchView searchSv = findViewById(R.id.searchSv);

        searchSv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String search = query;

                String forecast = "https://api.openweathermap.org/data/2.5/forecast?q=" + search + "&appid=86efc9be8662947df3dfa0f75fc53799";
                String currentForecast = "https://api.openweathermap.org/data/2.5/weather?q=" + search + "&appid=86efc9be8662947df3dfa0f75fc53799";

                RequestQueue requestQueue = Volley.newRequestQueue(WeatherApp.this);

                StringRequest forecastStringRequest = new StringRequest(
                        Request.Method.GET,
                        forecast,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                String jsonData = response;
                                Log.d("JSON DATA (Forecast):", jsonData);

                                jsonArrayDecode(jsonData, adapter);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(WeatherApp.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                    }
                });
                requestQueue.add(forecastStringRequest);

                StringRequest weatherStringRequest = new StringRequest(
                        Request.Method.GET,
                        currentForecast,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                String jsonData = response;
                                Log.d("JSON DATA (Weather):", jsonData);

                                parseWeather(jsonData);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(WeatherApp.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
                requestQueue.add(weatherStringRequest);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Update the SearchView query as the user types
//                searchSv.setQuery(newText, false);
                return true;
            }
        });
        searchIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchSv.setVisibility(View.VISIBLE);
            }
        });

    }

    private void jsonArrayDecode(String jsonData, RecyclerViewAdapter adapter) {
        ArrayList<ForecastModel> data = new ArrayList<>();
        String previousDay = "";

        try{
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray listArray = jsonObject.getJSONArray("list");

            for (int i = 0; i < listArray.length(); i++) {
                JSONObject listItem = listArray.getJSONObject(i);


                String date = convertTimestampToDate(listItem.getLong("dt"));
                String dayOfWeek = getDayOfWeek(date);
                String time = convertTimestampToTime(listItem.getLong("dt"));
                if (!dayOfWeek.equals(previousDay)) {
                    JSONObject mainObject = listItem.getJSONObject("main");
                    double temperatureInKelvin = mainObject.getDouble("temp");
                    double temperatureInCelsius = temperatureInKelvin - 273.15;
                    String temperature = String.format("%.2f", temperatureInCelsius) + " °C";

                    JSONArray weatherArray = listItem.getJSONArray("weather");
                    String iconCode = weatherArray.getJSONObject(0).getString("icon");
                    String imageUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";

                    data.add(new ForecastModel(imageUrl, dayOfWeek, time, temperature));
                }
                previousDay = dayOfWeek;
            }

            adapter.setDetails(data);

        }catch(JSONException e){
            e.printStackTrace();
        }
    }


    private String convertTimestampToTime(long timestamp) {
        Date date = new Date(timestamp * 1000);
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        return timeFormat.format(date);
    }


    private String parseWeather(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONObject mainObject = jsonObject.getJSONObject("main");

            JSONArray weatherArray = jsonObject.getJSONArray("weather");
            JSONObject weatherObject = weatherArray.getJSONObject(0);
            String weatherCondition = weatherObject.getString("main");

            String iconCode = weatherObject.getString("icon");
            String imageUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";

            String city = jsonObject.getString("name");


            long timestamp = jsonObject.getLong("dt");
            String date = convertTimestampToDate(timestamp);

            double temperatureInKelvin = mainObject.getDouble("temp");
            double temperatureInCelsius = temperatureInKelvin - 273.15;
            String temp = String.format("Temp \n"+" %.2f", temperatureInCelsius) + " °C";
            String humid = mainObject.getString("humidity")+ "mm";
//            String pressure = mainObject.getString("pressure")+ "%";
            double pressureValue = mainObject.getDouble("pressure");
            double pressurePercentage = calculatePressurePercentage(pressureValue);
            String formattedPressurePercentage = String.format("%.1f", pressurePercentage) + "%";
            pressureTv.setText("Pressure \n" + formattedPressurePercentage);

            String description = weatherObject.getString("description");

            JSONObject windData = jsonObject.getJSONObject("wind");
            String wind = windData.getString("speed")+ "mph";;


            windTv.setText("Wind \n"+wind);
            cityTv.setText(city);
            dateTv.setText(date);
            mainTempTv.setText(temp);
            descWeatherTv.setText(description);
            humidityTv.setText("Humidity \n"+humid);
            pressureTv.setText("Pressure \n"+formattedPressurePercentage);

            Picasso.get().load(imageUrl).into(mainIconTv);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonData;

    }

    private String convertTimestampToDate(long timestamp) {
        Date date = new Date(timestamp * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM, yyyy", Locale.ENGLISH);
        return sdf.format(date);

    }
    private double calculatePressurePercentage(double pressure) {
        double minPressure = 900; // Minimum pressure in the desired range
        double maxPressure = 1100; // Maximum pressure in the desired range

        double percentage = ((pressure - minPressure) / (maxPressure - minPressure)) * 100;
        return percentage;
    }

    private String getDayOfWeek(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM, yyyy", Locale.ENGLISH);
            Date parsedDate = sdf.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parsedDate);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            // Adjusting for Sunday (Calendar.SUNDAY = 1)
            if (dayOfWeek == Calendar.SUNDAY) {
                dayOfWeek = 8;
            }

            SimpleDateFormat sdfOutput = new SimpleDateFormat("EEEE", Locale.ENGLISH);
            return sdfOutput.format(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

}
