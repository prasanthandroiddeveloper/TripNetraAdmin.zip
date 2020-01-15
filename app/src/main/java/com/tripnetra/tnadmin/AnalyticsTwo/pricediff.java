package com.tripnetra.tnadmin.AnalyticsTwo;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tripnetra.tnadmin.R;
import com.tripnetra.tnadmin.adapters.DataAdapter;
import com.tripnetra.tnadmin.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class pricediff extends AppCompatActivity {

    Bundle bb;
    String SDate,EDate,type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pricediff);
        bb= getIntent().getExtras();
        assert bb!=null;
        type = bb.getString("type");
        SDate = bb.getString("FromDate");
        EDate = bb.getString("ToDate");
        getdata();
    }

    private void getdata() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://tripnetra.com/prasanth/androidphpfiles/adminpanel/getpricevar.php", response -> {

            Utils.showmsg(this,response);
            Utils.checkdata("response",response);
            try {

                RecyclerView recyclerView = findViewById(R.id.CNamesR);

                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(new LinearLayoutManager(pricediff.this));
                JSONArray jarr = new JSONArray(response);
                List<DataAdapter> list = new ArrayList<>();

                for(int i = 0; i< jarr.length(); i++) {
                    DataAdapter adapter = new DataAdapter();
                    JSONObject json = jarr.getJSONObject(i);

                    adapter.setHname(json.getString("name"));
                    adapter.setprc(json.getInt("tp"));

                    list.add(adapter);
                }

                recyclerView.setAdapter(new hotel_recycle_adapter(list));

            } catch (JSONException e) {


                Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();

            }


        }, error -> Toast.makeText(this, "Something went wrong.", Toast.LENGTH_LONG).show()){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();


                    params.put("type",type);
                    params.put("Sdate", SDate);
                    params.put("Edate", EDate);

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
        public hotel_recycle_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new hotel_recycle_adapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.pricerecycler, parent, false));
        }


        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull hotel_recycle_adapter.ViewHolder vh, int pos) {

            DataAdapter DDataAdapter =  listadapter.get(pos);

            vh.hnameTv.setText(DDataAdapter.getHname());
            vh.rcountTV.setText(String.valueOf(DDataAdapter.getprc()));



            int k= DDataAdapter.getprc();
            if(k<5000){
                vh.rcountTV.setTextColor(Color.parseColor("#F00D10"));
                vh.hnameTv.setTextColor(Color.parseColor("#F00D10"));
                vh.imageTv.setVisibility(View.VISIBLE);
                vh.imageTv.setBackgroundResource(R.drawable.ic_arrow_downward_black_24dp);
            }else if(k>5001 && k<19999){
                vh.rcountTV.setTextColor(Color.parseColor("#000080"));
                vh.hnameTv.setTextColor(Color.parseColor("#000080"));
                vh.imageTv.setVisibility(View.GONE);
            }else if(k>20000 ){
                vh.rcountTV.setTextColor(Color.parseColor("#008000"));
                vh.hnameTv.setTextColor(Color.parseColor("#008000"));
                vh.imageTv.setVisibility(View.GONE);
            }

        }

        @Override
        public int getItemCount() { return listadapter.size(); }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView hnameTv,rcountTV,imageTv;
            Bundle b=new Bundle();

            ViewHolder(View iv) {
                super(iv);

                context = iv.getContext();
                hnameTv = iv.findViewById(R.id.CNameTV);
                rcountTV = iv.findViewById(R.id.RoomCountTV);
                imageTv = iv.findViewById(R.id.ImageTV);


            }
        }
    }
}
