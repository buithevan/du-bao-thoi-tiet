package com.e.appdubaothoitiet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    EditText edtSearch;
    Button btnSearch,btnChangeActivity;
    TextView txtName,txtCounty,txtTemp,txtStatus,txtHumidity,txtCloud,txtWind,txtDay;
    ImageView imgIcon;
    String City = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Anhxa();
        GetCurrentWeatherData("Thai Nguyen");
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = edtSearch.getText().toString();
                if (city.equals("")){
                    City = "Thai Nguyen";
                    GetCurrentWeatherData(City);

                }else{
                    City = city;
                    GetCurrentWeatherData(City);
                }



            }
        });

        btnChangeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = edtSearch.getText().toString();
                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                intent.putExtra("name",city);
                startActivity(intent);

            }
        });


    }

    public void GetCurrentWeatherData(String data){
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://api.openweathermap.org/data/2.5/find?q="+data+"&units=metric&appid=1be7bbc3dc42b63e0781dfd02a508ee9";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArrayList = jsonObject.getJSONArray("list");
                            JSONObject jsonObjectList = jsonArrayList.getJSONObject(0);
                            String day =  jsonObjectList.getString("dt");
                            String name = jsonObjectList.getString("name");
                            txtName.setText("Tên thành phố : "+name);

                            long l = Long.valueOf(day);
                            Date date = new Date(l*1000L);
                            String pattern = "EEEE dd-MM-yyyy HH:mm:ss";
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                             day = simpleDateFormat.format(new Date());


                            txtDay.setText(day);
                            JSONArray jsonArrayWeather = jsonObjectList.getJSONArray("weather");
                            JSONObject jsonObject1Weather = jsonArrayWeather.getJSONObject(0);
                            String status = jsonObject1Weather.getString("main");
                            String icon = jsonObject1Weather.getString("icon");

                            Picasso.with(MainActivity.this).load("http://openweathermap.org/img/w/"+icon+".png").into(imgIcon);
                            txtStatus.setText(status);


                            JSONObject jsonObject1Main = jsonObjectList.getJSONObject("main");
                            String nhietdo = jsonObject1Main.getString("temp");
                            String doam = jsonObject1Main.getString("humidity");

                            Double a = Double.valueOf(nhietdo);
                            String Nhietdo = String.valueOf(a.intValue());
                            txtTemp.setText(Nhietdo+"°C");
                            txtHumidity.setText(doam+"%");



                            JSONObject jsonObject1Wind = jsonObjectList.getJSONObject("wind");
                            String gio = jsonObject1Wind.getString("speed");
                            txtWind.setText(gio+"m/s");


                            JSONObject jsonObject1Cloud = jsonObjectList.getJSONObject("clouds");
                            String may = jsonObject1Cloud.getString("all");
                            txtCloud.setText(may+"%");



                            JSONObject jsonObject1Sys = jsonObjectList.getJSONObject("sys");
                            String country = jsonObject1Sys.getString("country");
                            txtCounty.setText("Tên quốc gia : "+country);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }


        );
        requestQueue.add(stringRequest);
    }

    private void Anhxa() {
        edtSearch = (EditText) findViewById(R.id.edittextSearch);
        btnSearch = (Button) findViewById(R.id.buttonSearch);
        btnChangeActivity = (Button) findViewById(R.id.buttonChangeActivity);
        txtName = (TextView) findViewById(R.id.textviewName);
        txtCounty = (TextView) findViewById(R.id.textviewCountry);
        txtTemp = (TextView) findViewById(R.id.textviewTemp);
        txtStatus = (TextView) findViewById(R.id.textviewStatus);
        txtHumidity = (TextView) findViewById(R.id.textviewHumidity);
        txtCloud = (TextView) findViewById(R.id.textviewCloud);
        txtWind = (TextView) findViewById(R.id.textviewWind);
        txtDay = (TextView) findViewById(R.id.textviewDay);
        imgIcon =(ImageView) findViewById(R.id.imageIcon);



    }
}
