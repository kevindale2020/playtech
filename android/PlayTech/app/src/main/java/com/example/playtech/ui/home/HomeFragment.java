package com.example.playtech.ui.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.internal.Util;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.playtech.AppController;
import com.example.playtech.Product;
import com.example.playtech.ProductDetailsActivity;
import com.example.playtech.R;
import com.example.playtech.ProductAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private List<Product> productList;
    private ListView listView;
    private SwipeRefreshLayout refreshLayout;
    private ProductAdapter adapter;
    public static final String PID = "com.example.playtech.ui.home.PID";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        productList = new ArrayList<>();
        listView = root.findViewById(R.id.list);
        refreshLayout = root.findViewById(R.id.swipe_refresh_layout);
        adapter = new ProductAdapter(productList, getContext());

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                productList.clear();
                getProduts();
                refreshLayout.setRefreshing(false);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Product products = productList.get(position);
                //Toast.makeText(getContext(), "PID: " + products.getId(), Toast.LENGTH_SHORT).show();
                String productID = String.valueOf(products.getId());
                Intent intent = new Intent(getContext(), ProductDetailsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(PID, productID);
                startActivity(intent);
            }
        });

        getProduts();



        return root;
    }

    public void getProduts() {
        final ProgressDialog progressDialog;
        progressDialog = createProgressDialog(getContext());
        progressDialog.show();
        String url = "http://192.168.137.1:8081/PlayTech/MobileServlet";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("Products: ", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                progressDialog.dismiss();
                                JSONArray jsonArray = jsonObject.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    Product product = new Product();
                                    product.setId(object.getInt("id"));
                                    product.setImage(object.getString("image"));
                                    product.setName(object.getString("name"));
                                    product.setPrice(object.getDouble("price"));
                                    product.setStock(object.getInt("stock"));

                                    productList.add(product);
                                }
                                adapter = new ProductAdapter(productList, getContext());
                                listView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
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
                params.put("action", "mobile_product");

                return params;
            }
        };
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        //requestQueue.add(stringRequest);
        AppController.getmInstance().addToRequestQueue(stringRequest);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView)menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(TextUtils.isEmpty(s)) {
                    adapter.filter("");
                    listView.clearTextFilter();
                } else {
                    adapter.filter(s);
                }
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

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
