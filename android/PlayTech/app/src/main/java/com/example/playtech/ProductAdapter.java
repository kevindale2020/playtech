package com.example.playtech;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.playtech.Product;
import com.example.playtech.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ProductAdapter extends ArrayAdapter {

    public LayoutInflater inflater;
    public List<Product> productList;
    private List<Product> arrayList;
    public Context mCtx;

    public ProductAdapter(List<Product> productList, Context mCtx) {
        super(mCtx, R.layout.custom_product_layout, productList);
        this.productList = productList;
        this.arrayList = new ArrayList<>();
        this.arrayList.addAll(productList);
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
            convertView = inflater.inflate(R.layout.custom_product_layout, null, true);
        }

        //creating a view with our xml layout
        //final View listViewItem = inflater.inflate(R.layout.custom_layout, null, true);
        ImageView imageView = convertView.findViewById(R.id.image_view);
        TextView tv_name = convertView.findViewById(R.id.name);
        TextView tv_price = convertView.findViewById(R.id.price);
        TextView tv_stock = convertView.findViewById(R.id.stock);

        final Product products = productList.get(position);

        StringBuilder sb = new StringBuilder("http://192.168.137.1:8081/PlayTech/images/products/");
        sb.append(products.getImage());

        String imageURL = sb.toString();

        Picasso.get().load(imageURL).into(imageView);
        tv_name.setText(products.getName());
        tv_price.setText("₱" + String.format("%.2f", products.getPrice()));
        tv_stock.setText("Stock: " + products.getStock());

        return convertView;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        productList.clear();
        if (charText.length() == 0) {
            productList.addAll(arrayList);
        } else {
            for (Product products : arrayList) {
                if (products.getName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                        /*
                        if(pets.getStatus_id()==2) {
                            cardView.setVisibility(View.VISIBLE);
                            //next.setEnabled(false);
                            next.setClickable(false);
                        } else {
                            cardView.setVisibility(View.GONE);
                            //next.setEnabled(false);
                            next.setClickable(true);
                        }
                        */
                    productList.add(products);
                }
            }
        }
        notifyDataSetChanged();
    }
}
