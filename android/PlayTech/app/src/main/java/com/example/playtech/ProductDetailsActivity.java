package com.example.playtech;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.playtech.ui.home.HomeFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProductDetailsActivity extends AppCompatActivity {
    private String productID;
    private ImageView imageView;
    private TextView tv_status;
    private TextView tv_name;
    private TextView tv_price;
    private TextView tv_desc;
    private TextView tv_stock;
    private String imageURL;
    SessionManager sessionManager;
    private String user_id;
    private String quantity;
    private EditText input;
    private int stock = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // for the session values
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetails();
        user_id = user.get(sessionManager.USER_ID);

        // widget
        imageView = findViewById(R.id.image_view_details);
        tv_name = findViewById(R.id.name);
        tv_desc = findViewById(R.id.value_desc);
        tv_price = findViewById(R.id.price);
        tv_stock = findViewById(R.id.value_stock);
        tv_status = findViewById(R.id.status);

        // intent values
        Intent intent = getIntent();
        productID = intent.getStringExtra(HomeFragment.PID);

        getProductDetails();
    }

    public void getProductDetails() {
        final ProgressDialog progressDialog;
        progressDialog = createProgressDialog(ProductDetailsActivity.this);
        progressDialog.show();
        String url = "http://192.168.137.1:8081/PlayTech/MobileServlet";
        //String url = "http://192.168.43.44:8080/IRO/Android/login.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("Product Details: ", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                progressDialog.dismiss();
                                JSONArray jsonArray = jsonObject.getJSONArray("data");

                                JSONObject object = jsonArray.getJSONObject(0);

                                StringBuilder sb = new StringBuilder("http://192.168.137.1:8081/PlayTech/images/products/");
                                sb.append(object.getString("image"));

                                imageURL = sb.toString();

                                Picasso.get().load(imageURL).into(imageView);
                                tv_name.setText(object.getString("name"));
                                tv_desc.setText(object.getString("description"));
                                tv_price.setText("₱" + String.format("%.2f", object.getDouble("price")));
                                tv_stock.setText(String.valueOf(object.getInt("stock")));

                                stock = object.getInt("stock");

                                if (stock > 0) {
                                    tv_status.setText("Available");
                                    tv_status.setBackgroundColor(Color.parseColor("#5cb85c"));
                                } else {
                                    tv_status.setText("Out of Stock");
                                    tv_status.setBackgroundColor(Color.parseColor("#d9534f"));
                                }
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
                params.put("action", "mobile_product_details");
                params.put("pid", productID);

                return params;
            }
        };
        AppController.getmInstance().addToRequestQueue(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_details_menu, menu);

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

            case R.id.add_cart:

                if(stock == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailsActivity.this);

                    builder.setTitle("Out of Stock");
                    builder.setMessage("Product not available");

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    // Do nothing but close the dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailsActivity.this);

                    builder.setTitle("Add To Cart");
                    builder.setMessage("Quantity:");

                    //edit text
                    input = new EditText(ProductDetailsActivity.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
                    builder.setView(input);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog
                            dialog.dismiss();

                        }
                    });

                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                        }
                    });

                    final AlertDialog alert = builder.create();
                    alert.show();

                    alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            quantity = input.getText().toString().trim();
                            if (!validateInput()) {
                                return;
                            }

                            addToCart();
                            alert.hide();

                        }
                    });
                }

                break;
        }
        return super.

                onOptionsItemSelected(item);

    }

    public void addToCart() {
        final ProgressDialog progressDialog = new ProgressDialog(ProductDetailsActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        String url = "http://192.168.137.1:8081/PlayTech/MobileServlet";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("Add Cart: ", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            if (success.equals("1")) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailsActivity.this);

                                builder.setTitle("Add To Cart");
                                builder.setMessage(message);

                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                        // Do nothing but close the dialog
                                        progressDialog.dismiss();

//                                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        startActivity(intent);
//                                        finish();

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
                params.put("action", "mobile_add_cart");
                params.put("uid", user_id);
                params.put("pid", productID);
                params.put("qty", quantity);

                return params;
            }
        };
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        //requestQueue.add(stringRequest);
        AppController.getmInstance().addToRequestQueue(stringRequest);
    }

    public boolean validateInput() {
        if (quantity.isEmpty()) {
            input.setError("This is required");
            return false;
        } else if (Integer.parseInt(quantity) > stock) {
            input.setError("Exceeded the maximum stock");
            return false;
        } else if (Integer.parseInt(quantity) <= 0) {
            input.setError("Invalid input");
            return false;
        } else {
            input.setError(null);
            return true;
        }
    }

//    private void hideMenu() {
//
//        MenuItem item = menuList.findItem(R.id.add_cart);
//        item.setVisible(false);
//    }
//
//
//    private void showMenu() {
//
//        MenuItem item = menuList.findItem(R.id.add_cart);
//        item.setVisible(true);
//    }

    public static ProgressDialog createProgressDialog(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {

        }
        dialog.setCancelable(false);
        dialog.getWindow()
                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.progressdialog);
        // dialog.setMessage(Message);
        return dialog;
    }
}
