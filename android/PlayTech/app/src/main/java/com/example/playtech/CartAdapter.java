package com.example.playtech;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.playtech.ui.cart.CartFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;

public class CartAdapter extends ArrayAdapter {

    public LayoutInflater inflater;
    public List<Cart> cartList;
    public Context mCtx;
    public CartFragment cartFragment;

    public CartAdapter(List<Cart> cartList, Context mCtx, CartFragment cartFragment) {
        super(mCtx, R.layout.custom_cart_layout, cartList);
        this.cartList = cartList;
        this.mCtx = mCtx;
        this.cartFragment = cartFragment;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        inflater = LayoutInflater.from(mCtx);

        if (inflater == null) {
            inflater = (LayoutInflater) mCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_cart_layout, null, true);
        }

        //creating a view with our xml layout
        //final View listViewItem = inflater.inflate(R.layout.custom_layout, null, true);
        ImageView imageView = convertView.findViewById(R.id.image_view);
        ImageView iconDelete = convertView.findViewById(R.id.iconDelete);
        TextView tv_name = convertView.findViewById(R.id.name);
        TextView tv_price = convertView.findViewById(R.id.price);
        TextView tv_qty = convertView.findViewById(R.id.qty);

        final Cart carts = cartList.get(position);

        iconDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Remove from Cart");
                builder.setMessage("Are you sure you want to remove this item?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        remove(carts.getId());
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

        StringBuilder sb = new StringBuilder("http://192.168.137.1:8081/PlayTech/images/products/");
        sb.append(carts.getImage());

        String imageURL = sb.toString();

        Picasso.get().load(imageURL).into(imageView);
        tv_name.setText(carts.getName());
        tv_price.setText("₱" + String.format("%.2f", carts.getPrice()));
        tv_qty.setText("Quantity: " + carts.getQty());

        return convertView;
    }

    public void remove(final int id) {
//        final ProgressDialog progressDialog = new ProgressDialog(getContext());
//        progressDialog.setMessage("Please wait...");
//        progressDialog.show();
        String url = "http://192.168.137.1:8081/PlayTech/MobileServlet";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("Remove Cart: ", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            if (success.equals("1")) {
                                Log.e("Remove Result: ", message);
                                cartFragment.reload();

                            }
                        } catch (JSONException e) {
//                            progressDialog.dismiss();
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Failed" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        progressDialog.dismiss();
                        error.printStackTrace();
                        Toast.makeText(getContext(), "Failed" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "mobile_remove_cart");
                params.put("cid", String.valueOf(id));

                return params;
            }
        };
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        //requestQueue.add(stringRequest);
        AppController.getmInstance().addToRequestQueue(stringRequest);
    }
}
