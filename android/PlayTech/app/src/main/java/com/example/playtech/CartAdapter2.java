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

public class CartAdapter2 extends ArrayAdapter {

    public LayoutInflater inflater;
    public List<Cart> cartList;
    public Context mCtx;
    public CartFragment cartFragment;

    public CartAdapter2(List<Cart> cartList, Context mCtx) {
        super(mCtx, R.layout.custom_cart2_layout, cartList);
        this.cartList = cartList;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        inflater = LayoutInflater.from(mCtx);

        if (inflater == null) {
            inflater = (LayoutInflater) mCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_cart2_layout, null, true);
        }

        //creating a view with our xml layout
        //final View listViewItem = inflater.inflate(R.layout.custom_layout, null, true);
        ImageView imageView = convertView.findViewById(R.id.image_view);
        ImageView iconDelete = convertView.findViewById(R.id.iconDelete);
        TextView tv_name = convertView.findViewById(R.id.name);
        TextView tv_price = convertView.findViewById(R.id.price);
        TextView tv_qty = convertView.findViewById(R.id.qty);

        final Cart carts = cartList.get(position);

        StringBuilder sb = new StringBuilder("http://192.168.137.1:8081/PlayTech/images/products/");
        sb.append(carts.getImage());

        String imageURL = sb.toString();

        Picasso.get().load(imageURL).into(imageView);
        tv_name.setText(carts.getName());
        tv_price.setText("₱" + String.format("%.2f", carts.getPrice()));
        tv_qty.setText("Quantity: " + carts.getQty());

        return convertView;
    }
}
