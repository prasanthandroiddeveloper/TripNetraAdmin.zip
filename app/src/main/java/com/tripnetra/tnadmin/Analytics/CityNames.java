package com.tripnetra.tnadmin.Analytics;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;
import com.tripnetra.tnadmin.R;
import com.tripnetra.tnadmin.adapters.DataAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CityNames extends AppCompatActivity {


    private ProgressDialog pd;
    ArrayList<BarDataSet> yAxis;
    ArrayList<BarEntry> yValues;
    ArrayList<String> xAxis1;
    BarEntry values ;
    BarChart chart;
    BarData data;
    PieChart mChart;
    ArrayList<Entry> yVals1;
    ArrayList<String> xVals;
    Legend l;

    String pos,SDate,EDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_names);


        Bundle bb = getIntent().getExtras();
        assert bb!=null;
        pos = bb.getString("position");
        SDate = bb.getString("FromDate");
        EDate = bb.getString("ToDate");

        Log.i("b", String.valueOf(pos));

        chart = findViewById(R.id.chart);

        mChart =  findViewById(R.id.chart1);


        mChart.setCenterText("Employee Bookings");
        mChart.setCenterTextColor(Color.BLUE);
        mChart.setCenterTextSize(16f);
        getdata();
        load_data_from_server();

        mChart.setDrawSliceText(false);
        // To remove slice text

    }

    public void load_data_from_server() {

      String url = "https://tripnetra.com/androidphpfiles/adminpanel/gethotelnames.php";
        xAxis1 = new ArrayList<>();
        yAxis = null;
        yValues = new ArrayList<>();

        yVals1=new ArrayList<>();
        xVals=new ArrayList<>();


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                response -> {
                    Log.d("string",response);

                    try {

                        JSONArray jsonarray = new JSONArray(response);

                        for(int i=0; i < jsonarray.length(); i++) {

                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            String score = jsonobject.getString("roomcount").trim();
                            String name = jsonobject.getString("city_name").trim();


                            Log.i("score",score);
                            xAxis1.add(name);

                            values = new BarEntry(Float.valueOf(score),i);
                            yValues.add(values);

                            yVals1.add(new BarEntry(Float.parseFloat((score)), i));
                            xVals.add(name);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }


                    BarDataSet barDataSet1 = new BarDataSet(yValues, "City Names");
                    barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);
                    barDataSet1.setBarSpacePercent(150f);

                    yAxis = new ArrayList<>();
                    yAxis.add(barDataSet1);
                    String[] names = xAxis1.toArray(new String[0]);
                    data = new BarData(names,yAxis);
                    chart.setData(data);
                    chart.setDescription("");
                    chart.animateXY(2000, 2000);
                    chart.invalidate();

                    PieDataSet dataSet = new PieDataSet(yVals1, "City Names");
                    dataSet.setSliceSpace(3);
                    dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    PieData data = new PieData(xVals, dataSet);
                    data.setValueTextSize(9f);
                    data.setValueTextColor(Color.WHITE);
                    mChart.setData(data);
                    mChart.highlightValues(null);
                    mChart.invalidate();
                    mChart.animateXY(1400, 1400);
                    mChart.getData().setDrawValues(false);

                    l = mChart.getLegend(); // get legend of pie
                    l.getForm();
                    l.setFormSize(1f);
                    l.setYOffset(30f);
                    l.setXOffset(30f);

                    l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);

                    mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                        @Override
                        public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                            if (e == null)
                                return;

                            Toast.makeText(CityNames.this,
                                    xAxis1.get(e.getXIndex()) + " = " + e.getVal(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNothingSelected() {

                        }
                    });

                },
                error -> {
                    if(error != null){

                        Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();

                    }
                }
        ){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();

                params.put("type",pos);
                params.put("category","city_name");


                if(pos.equals("0")){
                    params.put("check_in_date", SDate);
                    params.put("check_out_date", EDate);

                } else{
                    params.put("book_date",SDate);
                    params.put("book_end_date",EDate);
                }
                Log.i("params", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }




    private void getdata() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://tripnetra.com/androidphpfiles/adminpanel/gethotelnames.php",response -> {


            try {

                RecyclerView recyclerView = findViewById(R.id.CNamesRV);

                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setLayoutManager(new LinearLayoutManager(CityNames.this));
                    JSONArray jarr = new JSONArray(response);
                    List<DataAdapter> list = new ArrayList<>();

                    for(int i = 0; i< jarr.length(); i++) {
                        DataAdapter adapter = new DataAdapter();
                        JSONObject json = jarr.getJSONObject(i);

                        adapter.setHname(json.getString("city_name"));
                        adapter.setRcunt(json.getString("roomcount"));

                        list.add(adapter);
                    }

                    recyclerView.setAdapter(new hotel_recycle_adapter(list));

            } catch (JSONException e) {


                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

            }


        }, error -> Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show()){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();

                params.put("type",pos);
                params.put("category","city_name");


                if(pos.equals("0")){
                    params.put("check_in_date", SDate);
                    params.put("check_out_date", EDate);

                } else{
                    params.put("book_date",SDate);
                    params.put("book_end_date",EDate);
                }
                Log.i("params", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        requestQueue.add(stringRequest);
    }

    public class hotel_recycle_adapter  extends RecyclerView.Adapter<hotel_recycle_adapter.ViewHolder>  {

        private Context context;
        private List<DataAdapter> listadapter;

        hotel_recycle_adapter(List<DataAdapter> list) {
            super();
            this.listadapter = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cnamesrecycler, parent, false));
        }


        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull hotel_recycle_adapter.ViewHolder vh, int pos) {

            DataAdapter DDataAdapter =  listadapter.get(pos);

            vh.hnameTv.setText(DDataAdapter.getHname());
            vh.rcountTV.setText(DDataAdapter.getRcunt());

        }

        @Override
        public int getItemCount() { return listadapter.size(); }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView hnameTv,rcountTV;
            Bundle b=new Bundle();

            ViewHolder(View iv) {
                super(iv);

                context = iv.getContext();
                hnameTv = iv.findViewById(R.id.CNameTV);
                rcountTV = iv.findViewById(R.id.RoomCountTV);


                iv.setOnClickListener(v -> {

                    Intent intent = new Intent(context, hotel_names.class);
                    b.putString("city_name", listadapter.get(getAdapterPosition()).getHname());
                    b.putString("room_count", listadapter.get(getAdapterPosition()).getRcunt());
                    b.putString("postn",pos);
                    b.putString("FromDate",SDate);
                    b.putString("ToDate",EDate);
                    intent.putExtras(b);
                    context.startActivity(intent);
                });
            }
        }
    }

}
