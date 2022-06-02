package com.example.playtech;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import com.example.playtech.ui.history.HistoryFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

public class OrderAdapter extends ArrayAdapter {
    public LayoutInflater inflater;
    public List<Order> orderList;
    public Context mCtx;
    public static final String ORDERNO = "com.example.playtech.OrderAdapter.ORDERNO";
    public EditText input;
    public String reason;
    public String user_id;
    public HistoryFragment historyFragment;

    public OrderAdapter(List<Order> orderList, String user_id, Context mCtx, HistoryFragment historyFragment) {
        super(mCtx, R.layout.custom_history_layout, orderList);
        this.orderList = orderList;
        this.mCtx = mCtx;
        this.user_id = user_id;
        this.historyFragment = historyFragment;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        inflater = LayoutInflater.from(mCtx);

        if (inflater == null) {
            inflater = (LayoutInflater) mCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_history_layout, null, true);
        }

        //creating a view with our xml layout
        //final View listViewItem = inflater.inflate(R.layout.custom_layout, null, true);
        ImageView imageView = convertView.findViewById(R.id.iconDetails);
        ImageView iconCancel = convertView.findViewById(R.id.iconCancel);
        ImageView btnReceive = convertView.findViewById(R.id.btnReceive);
        TextView tv_order_no = convertView.findViewById(R.id.orderno);
        TextView tv_date = convertView.findViewById(R.id.date);
        TextView tv_total = convertView.findViewById(R.id.total);
        TextView tv_status = convertView.findViewById(R.id.status);

        final Order orders = orderList.get(position);

        if(orders.getStatus().equals("Pending")) {
            iconCancel.setVisibility(View.VISIBLE);
        } else {
            iconCancel.setVisibility(View.GONE);
        }

        if(orders.getStatus().equals("Out for Delivery")) {
            btnReceive.setVisibility(View.VISIBLE);
        } else {
            btnReceive.setVisibility(View.GONE);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), OrderDetailsActivity.class);
                intent.putExtra(ORDERNO, orders.getOrderno());
                mCtx.startActivity(intent);
            }
        });

        iconCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Order Cancellation");
                builder.setMessage("Are you sure you want to cancel your order?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                        builder.setTitle("Order Cancellation");
                        builder.setMessage("Reason:");

                        //edit text
                        input = new EditText(getContext());
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        input.setLayoutParams(lp);
                        input.setInputType(InputType.TYPE_CLASS_TEXT);
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
                                reason = input.getText().toString().trim();
                                if (!validateInput()) {
                                    return;
                                }

                                cancelOrder(orders.getOrderno());
                                alert.hide();

                            }
                        });
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
            }
        });

        btnReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receiveOrder(orders.getOrderno());
            }
        });

        tv_order_no.setText("Order Number: " + orders.getOrderno());
        tv_date.setText("Order on: " + orders.getDate());
        tv_total.setText("Total Amount: ₱" + String.format("%.2f", orders.getTotal()));
        tv_status.setText(orders.getStatus());

        return convertView;
    }


    public void cancelOrder(final int orderno) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        String url = "http://192.168.137.1:8081/PlayTech/MobileServlet";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("Cancel Order: ", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            if (success.equals("1")) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                                builder.setTitle("Order Cancellation");
                                builder.setMessage(message);

                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                        // Do nothing but close the dialog
                                        progressDialog.dismiss();

//                                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        startActivity(intent);
//                                        finish();
                                        historyFragment.reload();

                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Failed" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        error.printStackTrace();
                        Toast.makeText(getContext(), "Failed" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "mobile_cancel_order");
                params.put("orderno", String.valueOf(orderno));
                params.put("reason", reason);

                return params;
            }
        };
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        //requestQueue.add(stringRequest);
        AppController.getmInstance().addToRequestQueue(stringRequest);
    }

    public void receiveOrder(final int orderno) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        String url = "http://192.168.137.1:8081/PlayTech/MobileServlet";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("Receive Order: ", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            if (success.equals("1")) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                                builder.setTitle("Order Completion");
                                builder.setMessage(message);

                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                        // Do nothing but close the dialog
                                        progressDialog.dismiss();

//                                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        startActivity(intent);
//                                        finish();
                                        historyFragment.reload();

                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Failed" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        error.printStackTrace();
                        Toast.makeText(getContext(), "Failed" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "mobile_receive_order");
                params.put("orderno", String.valueOf(orderno));

                return params;
            }
        };
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        //requestQueue.add(stringRequest);
        AppController.getmInstance().addToRequestQueue(stringRequest);
    }

    public boolean validateInput() {
        if (reason.isEmpty()) {
            input.setError("This is required");
            return false;
        }  else {
            input.setError(null);
            return true;
        }
    }
}
