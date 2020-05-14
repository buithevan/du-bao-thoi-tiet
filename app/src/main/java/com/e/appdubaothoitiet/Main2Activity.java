package com.e.appdubaothoitiet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Main2Activity extends AppCompatActivity {
    String tenthanhpho = "";
    ImageView imgback;
    TextView txtname;
    ListView lv;
    CustomAdapter customAdapter;
    ArrayList<Thoitiet> mangthoitiet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Anhxa();
        Intent intent = getIntent();
        String city = intent.getStringExtra("name");
        Log.d("ketqua","du lieu truyen qua : " + city);
        if (city.equals("")){
            tenthanhpho = "Thai Nguyen";
            Get7DaysData(tenthanhpho);
        }else {
            tenthanhpho = city;
            Get7DaysData(tenthanhpho);

        }
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private void Anhxa() {
        imgback = (ImageView) findViewById(R.id.imageviewback);
        txtname = (TextView) findViewById(R.id.textviewTenthanhpho);
        lv = (ListView) findViewById(R.id.listview);
        mangthoitiet = new ArrayList<Thoitiet>();
        customAdapter = new CustomAdapter(Main2Activity.this,mangthoitiet);
        lv.setAdapter(customAdapter);
    }

    private void Get7DaysData(String data) {
        String url = "https://api.openweathermap.org/data/2.5/forecast?q="+data+"&units=metric&appid=1be7bbc3dc42b63e0781dfd02a508ee9";
        RequestQueue requestQueue = Volley.newRequestQueue(Main2Activity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                            JSONObject jsonObject11City = jsonObject1.getJSONObject("city");
                            String name = jsonObject11City.getString("name");
                            txtname.setText(name);


                            JSONArray jsonArrayList = jsonObject1.getJSONArray("list");
                            for (int i=0;i<jsonArrayList.length();i++){
                                JSONObject jsonObject11List = jsonArrayList.getJSONObject(i);
                                String Day = jsonObject11List.getString("dt_txt");




                                JSONObject jsonObject11Temp = jsonObject11List.getJSONObject("main");
                                String max = jsonObject11Temp.getString("temp_max");
                                String min = jsonObject11Temp.getString("temp_min");

                                Double a = Double.valueOf(max);
                                Double b = Double.valueOf(min);
                                String NhietdoMax = String.valueOf(a.intValue());
                                String NhietdoMin = String.valueOf(b.intValue());

                                JSONArray jsonArrayWeather = jsonObject11List.getJSONArray("weather");
                                JSONObject jsonObject11weather = jsonArrayWeather.getJSONObject(0);
                                String status = jsonObject11weather.getString("description");
                                String icon = jsonObject11weather.getString("icon");

                                mangthoitiet.add(new Thoitiet(Day,status,icon,NhietdoMax,NhietdoMin));

                            }
                            customAdapter.notifyDataSetChanged();
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
}
