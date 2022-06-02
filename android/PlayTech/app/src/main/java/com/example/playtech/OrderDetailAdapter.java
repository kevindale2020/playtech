package com.example.playtech;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class OrderDetailAdapter extends ArrayAdapter {

    public LayoutInflater inflater;
    public List<OrderDetail> orderDetailList;
    public Context mCtx;

    public OrderDetailAdapter(List<OrderDetail> orderDetailList, Context mCtx) {
        super(mCtx, R.layout.custom_cart2_layout, orderDetailList);
        this.orderDetailList = orderDetailList;
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
        TextView tv_name = convertView.findViewById(R.id.name);
        TextView tv_price = convertView.findViewById(R.id.price);
        TextView tv_qty = convertView.findViewById(R.id.qty);

        final OrderDetail orderDetail = orderDetailList.get(position);

        StringBuilder sb = new StringBuilder("http://192.168.137.1:8081/PlayTech/images/products/");
        sb.append(orderDetail.getImage());

        String imageURL = sb.toString();

        Picasso.get().load(imageURL).into(imageView);
        tv_name.setText(orderDetail.getName());
        tv_price.setText("₱" + String.format("%.2f", orderDetail.getPrice()));
        tv_qty.setText("Quantity: " + orderDetail.getQty());

        return convertView;
    }
}
