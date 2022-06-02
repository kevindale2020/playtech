package com.example.playtech;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.playtech.ui.cart.CartFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {
    SessionManager sessionManager;
    private String user_id;
    private TextView tv_name;
    private TextView tv_contact;
    private TextView tv_address;
    private TextView tv_total;
    private TextView tv_date;
    private List<Cart> cartList;
    private ListView listView;
    private CartAdapter2 adapter;
    private double total = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
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

        cartList = new ArrayList<>();
        listView = findViewById(R.id.list);
        adapter = new CartAdapter2(cartList, getApplicationContext());

        // load delivery address
        tv_name.setText(user.get(sessionManager.FIRST_NAME) + " " + user.get(sessionManager.LAST_NAME));
        tv_contact.setText(user.get(sessionManager.CONTACT));
        tv_address.setText(user.get(sessionManager.ADDRESS));

        getCart();
        getEstimatedDate();

    }

    public void getCart() {
        String url = "http://192.168.137.1:8081/PlayTech/MobileServlet";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("Cart: ", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {

                                JSONArray jsonArray = jsonObject.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    Cart cart = new Cart();
                                    cart.setId(object.getInt("id"));
                                    cart.setImage(object.getString("image"));
                                    cart.setName(object.getString("name"));
                                    cart.setPrice(object.getDouble("price"));
                                    cart.setQty(object.getInt("qty"));

                                    total += (cart.getPrice() * cart.getQty());

                                    cartList.add(cart);
                                }
                                tv_total.setText("Total: ₱" + String.format("%.2f", total));
                                adapter = new CartAdapter2(cartList, getApplicationContext());
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
                params.put("action", "mobile_get_cart");
                params.put("uid", user_id);

                return params;
            }
        };
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        //requestQueue.add(stringRequest);
        AppController.getmInstance().addToRequestQueue(stringRequest);
    }

    public void getEstimatedDate() {
        String url = "http://192.168.137.1:8081/PlayTech/MobileServlet";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("Estimated Date: ", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String dateString = jsonObject.getString("finalDate");

                            if (success.equals("1")) {
                                tv_date.setText("(Est. Arrival: " + dateString + ")");
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
                params.put("action", "mobile_estimate_date");

                return params;
            }
        };
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        //requestQueue.add(stringRequest);
        AppController.getmInstance().addToRequestQueue(stringRequest);
    }

    public void placeOrder() {
//        loading.setVisibility(View.VISIBLE);
//        btnSave.setVisibility(View.GONE);
        final ProgressDialog progressDialog = new ProgressDialog(CheckoutActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        String url = "http://192.168.137.1:8081/PlayTech/MobileServlet";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("Place Order: ", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            final String message = jsonObject.getString("message");

                            if (success.equals("1")) {
                                progressDialog.dismiss();
//                                loading.setVisibility(View.GONE);
//                                btnSave.setVisibility(View.VISIBLE);

                                AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutActivity.this);

                                builder.setTitle("Place Order");
                                builder.setMessage(message);

                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                        // Do nothing but close the dialog
                                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Failed" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Failed" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "mobile_place_order");
                params.put("uid", user_id);

                return params;
            }
        };
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        //requestQueue.add(stringRequest);
        AppController.getmInstance().addToRequestQueue(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Add your menu entries here
        getMenuInflater().inflate(R.menu.checkout_menu, menu);

//        MenuItem menuItem = menu.findItem(R.id.add_cart);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                //action
                break;

            case R.id.place_order:
//                Toast.makeText(getApplicationContext(), "Hello World", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutActivity.this);

                builder.setTitle("Place Order");
                builder.setMessage("Are you sure you want to place your order?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        placeOrder();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
