package com.tripnetra.tnadmin.AnalyticsTwo;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmplyMnthlyBuss extends AppCompatActivity {
    Button CinBtn,CoutBtn,hotelBtn,tourBtn,darshanBtn;
    String FromDate,ToDate,Category;
    long mindate;int dateflag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empl_mnthly);
        CinBtn = findViewById(R.id.cinBtn);
        CoutBtn = findViewById(R.id.coutBtn);


        hotelBtn = findViewById(R.id.htlBtn);
        darshanBtn = findViewById(R.id.darBtn);
        tourBtn = findViewById(R.id.trBtn);

        hotelBtn.setOnClickListener(view -> {
            getdata();
            Category="htlemp";
        });

        darshanBtn.setOnClickListener(view -> {
            getdata();
            Category="daremp";
        });

        tourBtn.setOnClickListener(view -> {
            getdata();
            Category="touremp";
        });

        long fdate = System.currentTimeMillis(),tdate = System.currentTimeMillis();
        FromDate = Utils.DatetoStr(fdate,0);
        ToDate = Utils.DatetoStr(tdate,0);
        CinBtn.setOnClickListener(v -> {
            mindate = System.currentTimeMillis() - 7776000000L;
            dateflag = 1;
            datedialog();
        });

        CoutBtn.setOnClickListener(v -> {
            mindate = fdate;
            dateflag = 2;
            datedialog();
        });

    }

    public void datedialog(){

        final Calendar cal = Calendar.getInstance();

        DatePickerDialog pickerDialog = new DatePickerDialog(this, (view, year, month, day) -> {
            Calendar calndr = Calendar.getInstance();
            calndr.set(year,month,day);

            if (dateflag == 1) {

                Calendar ncal = Calendar.getInstance();
                ncal.setTime(calndr.getTime());
                ncal.add(Calendar.DATE, 1);

                FromDate = Utils.DatetoStr(calndr.getTime(),0);
                ToDate = Utils.DatetoStr(ncal.getTime(),0);
                CinBtn.setText(Utils.DatetoStr(calndr.getTime(),1));
                CoutBtn.setText(Utils.DatetoStr(ncal.getTime(),1));
                mindate = calndr.getTimeInMillis();
                dateflag = 2;
                datedialog();


            } else if (dateflag == 2) {

                Calendar ncal = Calendar.getInstance();
                ncal.setTime(calndr.getTime());
                ToDate = Utils.DatetoStr(calndr.getTime(),0);
                ncal.add(Calendar.DATE,1);
                CoutBtn.setText(Utils.DatetoStr(calndr.getTime(),1));

            }

        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        pickerDialog.show();

    }

    private void getdata() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://tripnetra.com/prasanth/androidphpfiles/adminpanel/getpricevar.php", response -> {

            Utils.showmsg(this,response);
            Utils.checkdata("response",response);
            try {

                RecyclerView recyclerView = findViewById(R.id.CNamesR);

                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(new LinearLayoutManager(EmplyMnthlyBuss.this));
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


                params.put("type",Category);
                params.put("Sdate", FromDate);
                params.put("Edate", ToDate);
                Utils.checkdata("ps4", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);

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
