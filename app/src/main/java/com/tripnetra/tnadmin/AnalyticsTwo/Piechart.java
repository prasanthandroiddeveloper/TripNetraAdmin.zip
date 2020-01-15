package com.tripnetra.tnadmin.AnalyticsTwo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.tripnetra.tnadmin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Piechart extends AppCompatActivity {
    PieChart mChart;
    ArrayList<Entry> yVals1;
    ArrayList<String> xVals;
    String FromDate="2019-12-13";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piechart);
        mChart =  findViewById(R.id.chart1);

        piechartdata();
        mChart.setCenterText("Employee Bookings");
        mChart.setCenterTextColor(Color.BLUE);
        mChart.setCenterTextSize(16f);
    }


    public void piechartdata() {

        yVals1=new ArrayList<>();
        xVals=new ArrayList<>();

        String url = "https://tripnetra.com/androidphpfiles/extranet/empnamebkngs.php";



        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                response -> {
                    Log.d("string",response);

                    try {

                        JSONArray jsonarray = new JSONArray(response);

                        for(int i=0; i < jsonarray.length(); i++) {

                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            float score = Float.parseFloat(jsonobject.getString("bcount"));
                            String name = jsonobject.getString("supported_by");
                            yVals1.add(new BarEntry((score), i));
                            xVals.add(name);

                        }

                        PieDataSet dataSet = new PieDataSet(yVals1, "");
                        dataSet.setSliceSpace(3);
                        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                        PieData data = new PieData(xVals, dataSet);
                        data.setValueTextSize(11f);
                        data.setValueTextColor(Color.WHITE);
                        mChart.setData(data);
                        mChart.highlightValues(null);
                        mChart.invalidate();
                        mChart.animateXY(1400, 1400);

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                },
                error -> Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show()
        ){

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params =new HashMap<>();

                params.put("date",FromDate);
                params.put("category","hotel");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }
}
