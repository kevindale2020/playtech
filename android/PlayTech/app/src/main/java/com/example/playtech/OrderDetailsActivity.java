package com.example.playtech;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetailsActivity extends AppCompatActivity {
    SessionManager sessionManager;
    private String user_id;
    private TextView tv_name;
    private TextView tv_contact;
    private TextView tv_address;
    private TextView tv_total;
    private TextView tv_date;
    private List<OrderDetail> orderDetailList;
    private ListView listView;
    private OrderDetailAdapter adapter;
    private int orderno;
    private double total = 0.0;
    private String ddate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // for the session values
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetails();
        user_id = user.get(sessionManager.USER_ID);

        // widget
        tv_name = findViewById(R.id.name);
        tv_contact = findViewById(R.id.contact);
        tv_address = findViewById(R.id.address);
        tv_total = findViewById(R.id.value_total);
        tv_date = findViewById(R.id.date);

        orderDetailList = new ArrayList<>();
        listView = findViewById(R.id.list);
        adapter = new OrderDetailAdapter(orderDetailList, getApplicationContext());

        // intent values
        Intent intent = getIntent();
        orderno = intent.getIntExtra(OrderAdapter.ORDERNO, 0);

        // load delivery address
        tv_name.setText(user.get(sessionManager.FIRST_NAME) + " " + user.get(sessionManager.LAST_NAME));
        tv_contact.setText(user.get(sessionManager.CONTACT));
        tv_address.setText(user.get(sessionManager.ADDRESS));

        getArrivalDate();
        getOrderDetailList();
    }

    public void getOrderDetailList() {
        String url = "http://192.168.137.1:8081/PlayTech/MobileServlet";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("Order Details: ", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {

                                JSONArray jsonArray = jsonObject.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    OrderDetail orderDetail = new OrderDetail(object.getInt("id"), object.getString("image"), object.getString("name"), object.getDouble("price"), object.getInt("qty"));

                                    total += (orderDetail.getPrice() * orderDetail.getQty());

                                    orderDetailList.add(orderDetail);
                                }
                                tv_total.setText("Total: ₱" + String.format("%.2f", total));
                                adapter = new OrderDetailAdapter(orderDetailList, getApplicationContext());
                                listView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Failed" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Failed" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "mobile_order_details_list");
                params.put("orderno", String.valueOf(orderno));

                return params;
            }
        };
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        //requestQueue.add(stringRequest);
        AppController.getmInstance().addToRequestQueue(stringRequest);
    }

    public void getArrivalDate() {
        String url = "http://192.168.137.1:8081/PlayTech/MobileServlet";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("Arrival Date: ", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String dateString = jsonObject.getString("finalDate");

                            if (success.equals("1")) {
                                tv_date.setText("(Arrival: " + dateString + ")");
                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Failed" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Failed" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "mobile_arrival_date");
                params.put("orderno", String.valueOf(orderno));

                return params;
            }
        };
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        //requestQueue.add(stringRequest);
        AppController.getmInstance().addToRequestQueue(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                //action
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
