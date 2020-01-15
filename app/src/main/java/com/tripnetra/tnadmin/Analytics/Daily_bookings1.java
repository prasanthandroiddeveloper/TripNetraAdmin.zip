package com.tripnetra.tnadmin.Analytics;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tripnetra.tnadmin.R;
import com.tripnetra.tnadmin.adapters.DataAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Daily_bookings1 extends AppCompatActivity {
    String date,Url="https://tripnetra.com/androidphpfiles/extranet/dailynew1.php";
    TextView hotel,tours,darshan;
    boolean open=false;
    RecyclerView recyclerView,recyclerView1,recyclerView2;
    CardView htl,tr,drshn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_bookings1);
        date=String.valueOf(android.text.format.DateFormat.format("yyyy-MM-dd", new java.util.Date()));
        Log.i("dailybookingdate",date);
        hotel=findViewById(R.id.hcTv);
        tours=findViewById(R.id.tcTv);
        darshan=findViewById(R.id.dcTv);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView1 = findViewById(R.id.recyclerview1);
        recyclerView2 = findViewById(R.id.recyclerview2);
        htl=findViewById(R.id.hotelview);
        tr=findViewById(R.id.tourview);
        drshn=findViewById(R.id.darshanview);

        hotels();
        tour();
        darshanam();
        hotelscount();
        tourcount();
        darshancount();
    }


    private void hotels() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, response -> {
            List<DataAdapter> list = new ArrayList<>();

            Log.i("res",response);
            try {

                JSONArray jsonarray = new JSONArray(response);

                for(int i=0; i < jsonarray.length(); i++) {

                    DataAdapter dataAdapter = new DataAdapter();

                    JSONObject jobj = jsonarray.getJSONObject(i);

                    dataAdapter.setHname(jobj.getString("hotel_name"));
                    dataAdapter.setHid("Rooms:"+jobj.getString("bcount"));
                    dataAdapter.setGname(jobj.getString("supported_by"));
                    dataAdapter.setMobile(jobj.getString("guest_count"));
                    list.add(dataAdapter);


                }

                recyclerView.setLayoutManager(new LinearLayoutManager(Daily_bookings1.this));
                recyclerView.setAdapter(new hotels_adapter(list));
            } catch (JSONException e) {
                e.printStackTrace();

                //  Toast.makeText(Daily_bookings1.this,e.getMessage(),Toast.LENGTH_SHORT).show();

            }

        }, error -> {

            Toast.makeText(Daily_bookings1.this, "Something went wrong.", Toast.LENGTH_LONG).show();

        })


        {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();


                params.put("date",date);
                params.put("category","hotel");



                Log.i("params", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Daily_bookings1.this);
        requestQueue.add(stringRequest);
    }
    private void tour() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, response -> {
            List<DataAdapter> list = new ArrayList<>();

            Log.i("res",response);
            try {

                JSONArray jsonarray = new JSONArray(response);

                for(int i=0; i < jsonarray.length(); i++) {

                    DataAdapter dataAdapter = new DataAdapter();

                    JSONObject jobj = jsonarray.getJSONObject(i);

                    dataAdapter.setHname(jobj.getString("sightseen_name"));
                    dataAdapter.setHid("Packages:"+jobj.getString("tcount"));
                    dataAdapter.setGname(jobj.getString("supported_by"));
                    dataAdapter.setMobile("A:"+jobj.getString("adults")+"C:"+jobj.getString("childs"));

                    list.add(dataAdapter);


                }

                recyclerView1.setLayoutManager(new LinearLayoutManager(Daily_bookings1.this));
                recyclerView1.setAdapter(new hotels_adapter(list));
            } catch (JSONException e) {
                e.printStackTrace();

                //  Toast.makeText(Daily_bookings1.this,e.getMessage(),Toast.LENGTH_SHORT).show();

            }

        }, error -> {

            // Toast.makeText(Daily_bookings1.this, "Something went wrong.", Toast.LENGTH_LONG).show();

        })


        {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();


                params.put("date",date);
                params.put("category","tour");


                Log.i("params", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(Daily_bookings1.this));
        requestQueue.add(stringRequest);
    }
    private void darshanam() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, response -> {
            List<DataAdapter> list = new ArrayList<>();

            Log.i("res",response);
            try {

                JSONArray jsonarray = new JSONArray(response);

                for(int i=0; i < jsonarray.length(); i++) {

                    DataAdapter dataAdapter = new DataAdapter();

                    JSONObject jobj = jsonarray.getJSONObject(i);

                    dataAdapter.setHname(jobj.getString("sightseen_name"));
                    dataAdapter.setHid("Packages:"+jobj.getString("darcount"));
                    dataAdapter.setGname(jobj.getString("supported_by"));
                    dataAdapter.setMobile("A:"+jobj.getString("adults")+"C:"+jobj.getString("childs"));
                    list.add(dataAdapter);


                }

                recyclerView2.setLayoutManager(new LinearLayoutManager(Daily_bookings1.this));
                recyclerView2.setAdapter(new hotels_adapter(list));
            } catch (JSONException e) {
                e.printStackTrace();

                //  Toast.makeText(Daily_bookings1.this,e.getMessage(),Toast.LENGTH_SHORT).show();

            }

        }, error -> {

            // Toast.makeText(Daily_bookings1.this, "Something went wrong.", Toast.LENGTH_LONG).show();

        })


        {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();


                params.put("date",date);
                params.put("category","darshan");


                Log.i("params", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(Daily_bookings1.this));
        requestQueue.add(stringRequest);
    }

    private void hotelscount() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, response -> {


            Log.i("res",response);
            try {

                JSONArray jsonarray = new JSONArray(response);

                for(int i=0; i < jsonarray.length(); i++) {

                    JSONObject jsonobject = jsonarray.getJSONObject(i);

                    String name = jsonobject.getString("bcount1");
                    hotel.setText(Daily_bookings1.this.getResources().getString(R.string.hotel)+" -- "+name);
                    if(!name.equals("0")){

                        htl.setVisibility(View.VISIBLE);

                    }
                    Log.i("name1", String.valueOf(name));
                }


            } catch (JSONException e) {
                e.printStackTrace();

            }

        }, error -> {

        })


        {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();


                params.put("date",date);
                params.put("category","hotel");
                params.put("type","notsingle");


                Log.i("params", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(Daily_bookings1.this));
        requestQueue.add(stringRequest);
    }

    private void tourcount() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, response -> {

            Log.i("res",response);
            try {

                JSONArray jsonarray = new JSONArray(response);

                for(int i=0; i < jsonarray.length(); i++) {

                    JSONObject jsonobject = jsonarray.getJSONObject(i);

                    String name = jsonobject.getString("tcount1");
                    tours.setText(Daily_bookings1.this.getResources().getString(R.string.tour1)+" -- "+name);

                    if(!name.equals("0")){

                        tr.setVisibility(View.VISIBLE);

                    }

                    Log.i("name1", String.valueOf(name));
                 //   Toast.makeText(Daily_bookings1.this,name,Toast.LENGTH_SHORT).show();

                }

            } catch (JSONException e) {
                e.printStackTrace();

            }

        }, error -> {

        })


        {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();

                params.put("date",date);
                params.put("category","tour");
                params.put("type","notsingle");

                Log.i("params", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(Daily_bookings1.this));
        requestQueue.add(stringRequest);
    }

    private void darshancount() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, response -> {


            Log.i("res",response);
            try {

                JSONArray jsonarray = new JSONArray(response);

                for(int i=0; i < jsonarray.length(); i++) {

                    JSONObject jsonobject = jsonarray.getJSONObject(i);

                    String name = jsonobject.getString("darcount1");
                    darshan.setText(Daily_bookings1.this.getResources().getString(R.string.darshan1)+" -- "+name);
                    if(!name.equals("0")){

                        drshn.setVisibility(View.VISIBLE);

                    }


                    Log.i("name1", String.valueOf(name));


                }


            } catch (JSONException e) {
                e.printStackTrace();

            }

        }, error -> {

            Toast.makeText(Daily_bookings1.this, "Something went wrong Try Again.", Toast.LENGTH_LONG).show();
        })


        {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();


                params.put("date",date);
                params.put("category","darshan");
                params.put("type","notsingle");


                Log.i("params", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(Daily_bookings1.this));
        requestQueue.add(stringRequest);
    }

    public void hotel(View view) {

        if(!open){
            open=true;
            htl.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);}

            else {
            open=false;
            htl.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);

        }
    }

    public void tour(View view) {
        if(!open){
            open=true;
            tr.setVisibility(View.VISIBLE);
            recyclerView1.setVisibility(View.VISIBLE);}

        else {
            open=false;
            tr.setVisibility(View.GONE);
            recyclerView1.setVisibility(View.GONE);

        }


    }

    public void Darshan(View view) {
        if(!open){
            open=true;
            drshn.setVisibility(View.VISIBLE);
            recyclerView2.setVisibility(View.VISIBLE);}

        else {
            open=false;
            drshn.setVisibility(View.GONE);
            recyclerView2.setVisibility(View.GONE);

        }


    }

    class hotels_adapter extends RecyclerView.Adapter<hotels_adapter.ViewHolder> {

        private List<DataAdapter> listadapter;

        hotels_adapter(List<DataAdapter> listAdapter) {
            super();
            this.listadapter = listAdapter;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.hotels_display, parent, false));
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView hotelnameTv,hotelcountTv,supportTv,PassTv;
            ViewHolder(View iv) {

                super(iv);

                hotelnameTv = iv.findViewById(R.id.hotelnameTv);
                hotelcountTv = iv.findViewById(R.id.hotelcountTv);
                supportTv = iv.findViewById(R.id.SupTv);
                PassTv = iv.findViewById(R.id.passTv);
                Context context = iv.getContext();



            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder vh, int pos) {

            DataAdapter dataAdap = listadapter.get(pos);
            vh.hotelnameTv.setText(dataAdap.getHname());
            vh.hotelcountTv.setText(dataAdap.getHid());
            vh.supportTv.setText(dataAdap.getGname());
            vh.PassTv.setText(dataAdap.getMobile());


        }

        @Override
        public int getItemCount() { return listadapter.size(); }
    }




}
